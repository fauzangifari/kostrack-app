package com.fauzangifari.kostrack.domain.model

enum class PaymentStatus {
    PENDING,
    PAID,
    OVERDUE,
    CANCELLED
}

enum class PaymentMethod {
    CASH,
    TRANSFER,
    EWALLET
}

data class Payment(
    val id: String,
    val tenantId: String,
    val roomId: String,
    val propertyId: String,
    val amount: Double,
    val dueDate: String,
    val paidDate: String?,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val method: PaymentMethod? = null,
    val receiptUrl: String?,
    val notes: String?,
    val createdAt: String? = null
)
