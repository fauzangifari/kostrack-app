package com.fauzangifari.kostrack.ui.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.usecase.auth.SignUpUseCase
import com.fauzangifari.kostrack.domain.usecase.session.SetLoggedInUseCase
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
    private val signUpUseCase: SignUpUseCase,
    private val setLoggedInUseCase: SetLoggedInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun signUp(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            _uiState.value = SignupUiState.Loading
            signUpUseCase(email, password, fullName)
                .onSuccess {
                    setLoggedInUseCase(true)
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
