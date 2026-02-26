package com.fauzangifari.kostrack.data.mapper

import com.fauzangifari.kostrack.data.model.dto.TenantDto
import com.fauzangifari.kostrack.domain.model.Tenant

fun TenantDto.toDomain(): Tenant {
    return Tenant(
        id = id ?: "",
        userId = userId,
        roomId = roomId,
        propertyId = propertyId,
        fullName = fullName,
        phone = phone,
        startDate = startDate,
        endDate = endDate,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun Tenant.toDto(): TenantDto {
    return TenantDto(
        id = if (id.isEmpty()) null else id,
        userId = userId,
        roomId = roomId,
        propertyId = propertyId,
        fullName = fullName,
        phone = phone,
        startDate = startDate,
        endDate = endDate,
        isActive = isActive,
        createdAt = createdAt
    )
}
