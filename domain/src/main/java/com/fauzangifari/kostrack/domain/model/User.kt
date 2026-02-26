package com.fauzangifari.kostrack.domain.model

enum class UserRole {
    OWNER,
    TENANT
}

data class User(
    val id: String,
    val email: String,
    val fullName: String?,
    val role: UserRole?
)
