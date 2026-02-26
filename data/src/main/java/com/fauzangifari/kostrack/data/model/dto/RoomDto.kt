package com.fauzangifari.kostrack.data.model.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class RoomDto(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("id") val id: String? = null,
    @SerialName("property_id") val propertyId: String,
    @SerialName("room_number") val roomNumber: String,
    @SerialName("price_per_month") val pricePerMonth: Double,
    @SerialName("status") val status: String = "AVAILABLE",
    @SerialName("facilities") val facilities: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("created_at") val createdAt: String? = null
)
