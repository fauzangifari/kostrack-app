package com.fauzangifari.kostrack.ui.screen.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.model.RoomStatus
import com.fauzangifari.kostrack.domain.usecase.room.GetRoomByIdUseCase
import com.fauzangifari.kostrack.domain.usecase.room.SaveRoomUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoomEditorState(
    val id: String = "",
    val propertyId: String = "",
    val roomNumber: String = "",
    val pricePerMonth: String = "",
    val status: RoomStatus = RoomStatus.AVAILABLE,
    val facilities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val error: String? = null
)

sealed class RoomEditorEvent {
    object Success : RoomEditorEvent()
    data class Error(val message: String) : RoomEditorEvent()
}

class RoomEditorViewModel(
    private val getRoomByIdUseCase: GetRoomByIdUseCase,
    private val saveRoomUseCase: SaveRoomUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomEditorState())
    val uiState: StateFlow<RoomEditorState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<RoomEditorEvent>()
    val event = _event.asSharedFlow()

    fun initEditor(propertyId: String, roomId: String?) {
        _uiState.update { it.copy(propertyId = propertyId) }

        if (roomId != null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, isEditMode = true) }
                getRoomByIdUseCase(roomId).onSuccess { room ->
                    _uiState.update {
                        it.copy(
                            id = room.id,
                            roomNumber = room.roomNumber,
                            pricePerMonth = room.pricePerMonth.toString(),
                            status = room.status,
                            facilities = room.facilities,
                            isLoading = false
                        )
                    }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    _event.emit(RoomEditorEvent.Error("Gagal memuat data kamar"))
                }
            }
        }
    }

    fun onRoomNumberChange(value: String) = _uiState.update { it.copy(roomNumber = value) }
    fun onPriceChange(value: String) = _uiState.update { it.copy(pricePerMonth = value) }
    fun onStatusChange(value: RoomStatus) = _uiState.update { it.copy(status = value) }

    fun toggleFacility(facility: String) {
        _uiState.update {
            val updated = it.facilities.toMutableList()
            if (updated.contains(facility)) updated.remove(facility) else updated.add(facility)
            it.copy(facilities = updated)
        }
    }

    fun addCustomFacility(facility: String) {
        if (facility.isBlank()) return
        _uiState.update {
            if (it.facilities.contains(facility)) it
            else it.copy(facilities = it.facilities + facility)
        }
    }

    fun saveRoom() {
        val state = _uiState.value
        if (state.roomNumber.isBlank() || state.pricePerMonth.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val room = Room(
                id = state.id,
                propertyId = state.propertyId,
                roomNumber = state.roomNumber,
                pricePerMonth = state.pricePerMonth.toDoubleOrNull() ?: 0.0,
                status = state.status,
                facilities = state.facilities
            )

            saveRoomUseCase(room)
                .onSuccess {
                    _event.emit(RoomEditorEvent.Success)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.emit(RoomEditorEvent.Error(e.message ?: "Gagal menyimpan kamar"))
                }
        }
    }
}
