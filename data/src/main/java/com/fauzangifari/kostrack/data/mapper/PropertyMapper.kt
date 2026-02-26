package com.fauzangifari.kostrack.data.mapper

import com.fauzangifari.kostrack.data.model.dto.PropertyDto
import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.model.PropertyType

fun PropertyDto.toDomain(): Property {
    return Property(
        id = id ?: "",
        ownerId = ownerId,
        name = name,
        address = address,
        description = description,
        imageUrl = imageUrl,
        type = try {
            PropertyType.valueOf(type)
        } catch (e: Exception) {
            PropertyType.CAMPUR
        },
        createdAt = createdAt
    )
}

fun Property.toDto(): PropertyDto {
    return PropertyDto(
        id = if (id.isEmpty()) null else id,
        ownerId = ownerId,
        name = name,
        address = address,
        description = if (description.isNullOrBlank()) null else description,
        imageUrl = if (imageUrl.isNullOrBlank()) null else imageUrl,
        type = type.name,
        createdAt = createdAt
    )
}
