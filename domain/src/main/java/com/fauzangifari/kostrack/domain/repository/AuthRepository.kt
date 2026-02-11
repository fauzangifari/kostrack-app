package com.fauzangifari.kostrack.domain.repository

import com.fauzangifari.kostrack.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): Flow<User?>
}
