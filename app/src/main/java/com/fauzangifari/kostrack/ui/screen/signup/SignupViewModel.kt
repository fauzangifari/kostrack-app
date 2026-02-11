package com.fauzangifari.kostrack.ui.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.repository.AuthRepository
import com.fauzangifari.kostrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SignupUiState {
    object Idle : SignupUiState()
    object Loading : SignupUiState()
    data class Success(val message: String) : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}

class SignupViewModel(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun signUp(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            _uiState.value = SignupUiState.Loading
            authRepository.signUp(email, password, fullName)
                .onSuccess {
                    // We automatically log them in or just set flag after signup success
                    sessionRepository.setLoggedIn(true)
                    _uiState.value = SignupUiState.Success("Registration successful!")
                }
                .onFailure {
                    _uiState.value = SignupUiState.Error(it.message ?: "An error occurred during registration")
                }
        }
    }

    fun resetState() {
        _uiState.value = SignupUiState.Idle
    }
}
