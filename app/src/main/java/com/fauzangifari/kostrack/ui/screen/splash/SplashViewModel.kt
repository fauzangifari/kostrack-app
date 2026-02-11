package com.fauzangifari.kostrack.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.fauzangifari.kostrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class SplashViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {
    val isLoggedIn: Flow<Boolean> = sessionRepository.isLoggedIn()
}