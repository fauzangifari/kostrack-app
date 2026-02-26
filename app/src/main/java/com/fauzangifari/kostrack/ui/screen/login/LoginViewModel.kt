package com.fauzangifari.kostrack.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.usecase.auth.SignInUseCase
import com.fauzangifari.kostrack.domain.usecase.session.SetLoggedInUseCase
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
    private val signInUseCase: SignInUseCase,
    private val setLoggedInUseCase: SetLoggedInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            signInUseCase(email, password)
                .onSuccess {
                    setLoggedInUseCase(true)
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
