package com.fauzangifari.kostrack.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.User
import com.fauzangifari.kostrack.domain.usecase.auth.GetCurrentUserUseCase
import com.fauzangifari.kostrack.domain.usecase.auth.SignOutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProfileEvent {
    object LogoutSuccess : ProfileEvent()
    data class Error(val message: String) : ProfileEvent()
}

class ProfileViewModel(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val currentUser: StateFlow<User?> = getCurrentUserUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event: SharedFlow<ProfileEvent> = _event.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            signOutUseCase()
                .onSuccess {
                    _event.emit(ProfileEvent.LogoutSuccess)
                }
                .onFailure { e ->
                    _event.emit(ProfileEvent.Error(e.message ?: "Gagal logout"))
                }
        }
    }
}
