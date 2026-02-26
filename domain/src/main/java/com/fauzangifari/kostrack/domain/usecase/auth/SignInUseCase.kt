package com.fauzangifari.kostrack.domain.usecase.auth

import com.fauzangifari.kostrack.domain.model.User
import com.fauzangifari.kostrack.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email tidak boleh kosong"))
        if (password.isBlank()) return Result.failure(IllegalArgumentException("Password tidak boleh kosong"))
        return authRepository.signIn(email, password)
    }
}
