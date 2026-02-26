package com.fauzangifari.kostrack.domain.model

enum class RoomStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE
}

data class Room(
    val id: String,
    val propertyId: String,
    val roomNumber: String,
    val pricePerMonth: Double,
    val status: RoomStatus = RoomStatus.AVAILABLE,
    val facilities: List<String> = emptyList(),
    val createdAt: String? = null
)
