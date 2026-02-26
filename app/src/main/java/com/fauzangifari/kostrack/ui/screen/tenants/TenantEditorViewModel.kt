package com.fauzangifari.kostrack.ui.screen.tenants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Tenant
import com.fauzangifari.kostrack.domain.usecase.auth.GetCurrentUserUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.AssignTenantUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TenantEditorState(
    val propertyId: String = "",
    val roomId: String = "",
    val fullName: String = "",
    val phone: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isLoading: Boolean = false
)

sealed class TenantEditorEvent {
    object Success : TenantEditorEvent()
    data class Error(val message: String) : TenantEditorEvent()
}

class TenantEditorViewModel(
    private val assignTenantUseCase: AssignTenantUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TenantEditorState())
    val uiState: StateFlow<TenantEditorState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TenantEditorEvent>()
    val event = _event.asSharedFlow()

    fun initEditor(propertyId: String, roomId: String) {
        _uiState.update { it.copy(propertyId = propertyId, roomId = roomId) }
    }

    fun onFullNameChange(value: String) = _uiState.update { it.copy(fullName = value) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value) }
    fun onStartDateChange(value: String) = _uiState.update { it.copy(startDate = value) }
    fun onEndDateChange(value: String) = _uiState.update { it.copy(endDate = value) }

    fun assignTenant() {
        val state = _uiState.value
        if (state.fullName.isBlank() || state.startDate.isBlank()) {
            viewModelScope.launch {
                _event.emit(TenantEditorEvent.Error("Nama dan tanggal mulai wajib diisi"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val user = getCurrentUserUseCase().firstOrNull()
            if (user == null) {
                _uiState.update { it.copy(isLoading = false) }
                _event.emit(TenantEditorEvent.Error("Gagal mendapatkan data user"))
                return@launch
            }

            val tenant = Tenant(
                id = "",
                userId = user.id,
                roomId = state.roomId,
                propertyId = state.propertyId,
                fullName = state.fullName,
                phone = state.phone.ifBlank { null },
                startDate = state.startDate,
                endDate = state.endDate.ifBlank { null },
                isActive = true
            )

            assignTenantUseCase(tenant)
                .onSuccess {
                    _event.emit(TenantEditorEvent.Success)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.emit(TenantEditorEvent.Error(e.message ?: "Gagal menambahkan penghuni"))
                }
        }
    }
}
