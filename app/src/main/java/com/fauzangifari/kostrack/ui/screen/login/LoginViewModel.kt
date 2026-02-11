package com.fauzangifari.kostrack.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.repository.AuthRepository
import com.fauzangifari.kostrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            authRepository.signIn(email, password)
                .onSuccess {
                    sessionRepository.setLoggedIn(true)
                    _uiState.value = LoginUiState.Success("Login successful!")
                }
                .onFailure {
                    _uiState.value = LoginUiState.Error(it.message ?: "Invalid Email or Password")
                }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}
