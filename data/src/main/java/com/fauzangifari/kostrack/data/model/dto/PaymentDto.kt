package com.fauzangifari.kostrack.data.model.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PaymentDto(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("id") val id: String? = null,
    @SerialName("tenant_id") val tenantId: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("property_id") val propertyId: String,
    @SerialName("amount") val amount: Double,
    @SerialName("due_date") val dueDate: String,
    @SerialName("paid_date") val paidDate: String? = null,
    @SerialName("status") val status: String = "PENDING",
    @SerialName("method") val method: String? = null,
    @SerialName("receipt_url") val receiptUrl: String? = null,
    @SerialName("notes") val notes: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("created_at") val createdAt: String? = null
)
