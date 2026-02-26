package com.fauzangifari.kostrack.domain.usecase.session

import com.fauzangifari.kostrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class IsLoggedInUseCase(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(): Flow<Boolean> = sessionRepository.isLoggedIn()
}
