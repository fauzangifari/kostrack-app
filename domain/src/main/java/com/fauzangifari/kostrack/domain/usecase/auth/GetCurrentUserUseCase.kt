package com.fauzangifari.kostrack.domain.usecase.auth

import com.fauzangifari.kostrack.domain.model.User
import com.fauzangifari.kostrack.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> = authRepository.getCurrentUser()
}
