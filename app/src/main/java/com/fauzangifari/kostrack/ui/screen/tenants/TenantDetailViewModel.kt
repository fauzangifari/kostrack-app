package com.fauzangifari.kostrack.ui.screen.tenants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Tenant
import com.fauzangifari.kostrack.domain.usecase.tenant.RemoveTenantUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.fauzangifari.kostrack.domain.repository.TenantRepository

data class TenantDetailState(
    val tenant: Tenant? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class TenantDetailEvent {
    object RemoveSuccess : TenantDetailEvent()
    data class Error(val message: String) : TenantDetailEvent()
}

class TenantDetailViewModel(
    private val tenantRepository: TenantRepository,
    private val removeTenantUseCase: RemoveTenantUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TenantDetailState())
    val uiState: StateFlow<TenantDetailState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TenantDetailEvent>()
    val event = _event.asSharedFlow()

    fun loadTenant(tenantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            tenantRepository.getTenantById(tenantId)
                .onSuccess { tenant ->
                    _uiState.update { it.copy(tenant = tenant, isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun removeTenant() {
        val tenant = _uiState.value.tenant ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            removeTenantUseCase(tenant.id, tenant.roomId)
                .onSuccess {
                    _event.emit(TenantDetailEvent.RemoveSuccess)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.emit(TenantDetailEvent.Error(e.message ?: "Gagal menghapus penghuni"))
                }
        }
    }
}
