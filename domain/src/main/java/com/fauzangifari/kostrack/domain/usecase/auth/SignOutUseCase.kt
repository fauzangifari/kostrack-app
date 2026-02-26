package com.fauzangifari.kostrack.domain.usecase.auth

import com.fauzangifari.kostrack.domain.repository.AuthRepository
import com.fauzangifari.kostrack.domain.repository.SessionRepository

class SignOutUseCase(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val result = authRepository.signOut()
        if (result.isSuccess) {
            sessionRepository.clearSession()
        }
        return result
    }
}
