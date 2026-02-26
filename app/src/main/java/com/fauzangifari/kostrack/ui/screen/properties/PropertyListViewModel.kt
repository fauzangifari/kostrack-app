package com.fauzangifari.kostrack.ui.screen.properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.usecase.property.DeletePropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.property.GetPropertiesUseCase
import com.fauzangifari.kostrack.domain.usecase.room.GetRoomsByPropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.GetTenantsByPropertyUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PropertyWithRoomInfo(
    val property: Property,
    val totalRooms: Int = 0,
    val occupiedRooms: Int = 0
)

sealed class PropertyListState {
    object Loading : PropertyListState()
    data class Success(val properties: List<PropertyWithRoomInfo>) : PropertyListState()
    data class Error(val message: String) : PropertyListState()
    object Empty : PropertyListState()
}

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyListViewModel(
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
    private val getRoomsByPropertyUseCase: GetRoomsByPropertyUseCase,
    private val getTenantsByPropertyUseCase: GetTenantsByPropertyUseCase
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    val uiState: StateFlow<PropertyListState> = refreshTrigger
        .flatMapLatest {
            getPropertiesUseCase()
                .map { properties ->
                    if (properties.isEmpty()) {
                        PropertyListState.Empty
                    } else {
                        val withRoomInfo = properties.map { property ->
                            val rooms = getRoomsByPropertyUseCase(property.id).firstOrNull() ?: emptyList()
                            val tenants = getTenantsByPropertyUseCase(property.id).firstOrNull() ?: emptyList()
                            val occupiedRoomIds = tenants.map { it.roomId }.toSet()
                            PropertyWithRoomInfo(
                                property = property,
                                totalRooms = rooms.size,
                                occupiedRooms = rooms.count { it.id in occupiedRoomIds }
                            )
                        }
                        PropertyListState.Success(withRoomInfo)
                    }
                }
        }
        .onStart { emit(PropertyListState.Loading) }
        .catch { emit(PropertyListState.Error(it.message ?: "Terjadi kesalahan")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PropertyListState.Loading
        )

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }

    fun deleteProperty(id: String) {
        viewModelScope.launch {
            deletePropertyUseCase(id).onSuccess {
                refresh()
            }
        }
    }
}
