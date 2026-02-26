package com.fauzangifari.kostrack.domain.usecase.session

import com.fauzangifari.kostrack.domain.repository.SessionRepository

class SetLoggedInUseCase(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(isLoggedIn: Boolean) {
        sessionRepository.setLoggedIn(isLoggedIn)
    }
}
