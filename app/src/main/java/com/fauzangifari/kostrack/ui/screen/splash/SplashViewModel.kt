package com.fauzangifari.kostrack.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.fauzangifari.kostrack.domain.usecase.session.IsLoggedInUseCase
import kotlinx.coroutines.flow.Flow

class SplashViewModel(
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {
    val isLoggedIn: Flow<Boolean> = isLoggedInUseCase()
}