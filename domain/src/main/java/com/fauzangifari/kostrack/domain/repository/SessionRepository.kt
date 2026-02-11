package com.fauzangifari.kostrack.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun isLoggedIn(): Flow<Boolean>
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    suspend fun clearSession()
}
