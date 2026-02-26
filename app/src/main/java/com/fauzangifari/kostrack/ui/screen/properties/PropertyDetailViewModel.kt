package com.fauzangifari.kostrack.ui.screen.properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.model.Tenant
import com.fauzangifari.kostrack.domain.usecase.property.GetPropertyByIdUseCase
import com.fauzangifari.kostrack.domain.usecase.room.DeleteRoomUseCase
import com.fauzangifari.kostrack.domain.usecase.room.GetRoomsByPropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.GetTenantsByPropertyUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PropertyDetailState(
    val property: Property? = null,
    val rooms: List<Room> = emptyList(),
    val tenants: List<Tenant> = emptyList(),
    val tenantByRoomId: Map<String, Tenant> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PropertyDetailViewModel(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    private val getRoomsByPropertyUseCase: GetRoomsByPropertyUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase,
    private val getTenantsByPropertyUseCase: GetTenantsByPropertyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PropertyDetailState())
    val uiState: StateFlow<PropertyDetailState> = _uiState.asStateFlow()

    fun loadDetail(propertyId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getPropertyByIdUseCase(propertyId)
                .onSuccess { property ->
                    _uiState.update { it.copy(property = property) }

                    launch {
                        getRoomsByPropertyUseCase(propertyId).collect { rooms ->
                            _uiState.update { it.copy(rooms = rooms, isLoading = false) }
                        }
                    }

                    launch {
                        getTenantsByPropertyUseCase(propertyId).collect { tenants ->
                            val tenantMap = tenants
                                .filter { it.isActive }
                                .associateBy { it.roomId }
                            _uiState.update { it.copy(tenants = tenants, tenantByRoomId = tenantMap) }
                        }
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun deleteRoom(roomId: String) {
        viewModelScope.launch {
            deleteRoomUseCase(roomId)
        }
    }
}
