package com.fauzangifari.kostrack.domain.model

enum class PropertyType {
    PUTRA,
    PUTRI,
    CAMPUR
}

data class Property(
    val id: String,
    val ownerId: String,
    val name: String,
    val address: String,
    val description: String?,
    val imageUrl: String?,
    val type: PropertyType = PropertyType.CAMPUR,
    val createdAt: String? = null
)
