package com.fauzangifari.kostrack.data.mapper

import com.fauzangifari.kostrack.data.model.dto.RoomDto
import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.model.RoomStatus

fun RoomDto.toDomain(): Room {
    return Room(
        id = id ?: "",
        propertyId = propertyId,
        roomNumber = roomNumber,
        pricePerMonth = pricePerMonth,
        status = try {
            RoomStatus.valueOf(status)
        } catch (e: Exception) {
            RoomStatus.AVAILABLE
        },
        facilities = facilities ?: emptyList(),
        createdAt = createdAt
    )
}

fun Room.toDto(): RoomDto {
    return RoomDto(
        id = if (id.isEmpty()) null else id,
        propertyId = propertyId,
        roomNumber = roomNumber,
        pricePerMonth = pricePerMonth,
        status = status.name,
        facilities = facilities.ifEmpty { null },
        createdAt = createdAt
    )
}
