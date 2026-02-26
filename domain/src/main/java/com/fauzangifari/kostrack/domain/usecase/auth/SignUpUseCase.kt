package com.fauzangifari.kostrack.domain.usecase.auth

import com.fauzangifari.kostrack.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, fullName: String): Result<Unit> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email tidak boleh kosong"))
        if (password.length < 6) return Result.failure(IllegalArgumentException("Password minimal 6 karakter"))
        if (fullName.isBlank()) return Result.failure(IllegalArgumentException("Nama lengkap tidak boleh kosong"))
        return authRepository.signUp(email, password, fullName)
    }
}
