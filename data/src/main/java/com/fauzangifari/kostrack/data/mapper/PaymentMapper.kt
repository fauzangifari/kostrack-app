package com.fauzangifari.kostrack.data.mapper

import com.fauzangifari.kostrack.data.model.dto.PaymentDto
import com.fauzangifari.kostrack.domain.model.Payment
import com.fauzangifari.kostrack.domain.model.PaymentMethod
import com.fauzangifari.kostrack.domain.model.PaymentStatus

fun PaymentDto.toDomain(): Payment {
    return Payment(
        id = id ?: "",
        tenantId = tenantId,
        roomId = roomId,
        propertyId = propertyId,
        amount = amount,
        dueDate = dueDate,
        paidDate = paidDate,
        status = try {
            PaymentStatus.valueOf(status)
        } catch (e: Exception) {
            PaymentStatus.PENDING
        },
        method = method?.let {
            try {
                PaymentMethod.valueOf(it)
            } catch (e: Exception) {
                null
            }
        },
        receiptUrl = receiptUrl,
        notes = notes,
        createdAt = createdAt
    )
}

fun Payment.toDto(): PaymentDto {
    return PaymentDto(
        id = if (id.isEmpty()) null else id,
        tenantId = tenantId,
        roomId = roomId,
        propertyId = propertyId,
        amount = amount,
        dueDate = dueDate,
        paidDate = paidDate,
        status = status.name,
        method = method?.name,
        receiptUrl = receiptUrl,
        notes = notes,
        createdAt = createdAt
    )
}
