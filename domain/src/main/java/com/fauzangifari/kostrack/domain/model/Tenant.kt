package com.fauzangifari.kostrack.domain.model

data class Tenant(
    val id: String,
    val userId: String,
    val roomId: String,
    val propertyId: String,
    val fullName: String,
    val phone: String?,
    val startDate: String,
    val endDate: String?,
    val isActive: Boolean = true,
    val createdAt: String? = null
)
