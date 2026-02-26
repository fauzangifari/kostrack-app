package com.fauzangifari.kostrack.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.model.RoomStatus
import com.fauzangifari.kostrack.domain.model.User
import com.fauzangifari.kostrack.domain.usecase.auth.GetCurrentUserUseCase
import com.fauzangifari.kostrack.domain.usecase.property.GetPropertiesUseCase
import com.fauzangifari.kostrack.domain.usecase.room.GetRoomsByPropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.GetTenantsByPropertyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PropertySummary(
    val property: Property,
    val totalRooms: Int = 0,
    val occupiedRooms: Int = 0
)

data class DashboardState(
    val propertySummaries: List<PropertySummary> = emptyList(),
    val totalRooms: Int = 0,
    val occupiedRooms: Int = 0,
    val totalTenants: Int = 0,
    val isLoading: Boolean = false
)

class DashboardViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getRoomsByPropertyUseCase: GetRoomsByPropertyUseCase,
    private val getTenantsByPropertyUseCase: GetTenantsByPropertyUseCase
) : ViewModel() {

    val currentUser: StateFlow<User?> = getCurrentUserUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _dashboardState.update { it.copy(isLoading = true) }

            getPropertiesUseCase().collect { properties ->
                var totalRooms = 0
                var totalOccupied = 0
                var totalTenants = 0
                val summaries = mutableListOf<PropertySummary>()

                for (property in properties) {
                    val rooms = getRoomsByPropertyUseCase(property.id).firstOrNull() ?: emptyList()
                    val tenants = getTenantsByPropertyUseCase(property.id).firstOrNull() ?: emptyList()
                    val occupied = rooms.count { it.status == RoomStatus.OCCUPIED }
                    val activeTenants = tenants.count { it.isActive }

                    totalRooms += rooms.size
                    totalOccupied += occupied
                    totalTenants += activeTenants

                    summaries.add(
                        PropertySummary(
                            property = property,
                            totalRooms = rooms.size,
                            occupiedRooms = occupied
                        )
                    )
                }

                _dashboardState.update {
                    it.copy(
                        propertySummaries = summaries,
                        totalRooms = totalRooms,
                        occupiedRooms = totalOccupied,
                        totalTenants = totalTenants,
                        isLoading = false
                    )
                }
            }
        }
    }
}
