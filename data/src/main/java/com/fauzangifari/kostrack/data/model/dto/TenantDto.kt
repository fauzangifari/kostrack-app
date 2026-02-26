package com.fauzangifari.kostrack.data.model.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TenantDto(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("property_id") val propertyId: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("phone") val phone: String? = null,
    @SerialName("check_in_date") val startDate: String,
    @SerialName("check_out_date") val endDate: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("created_at") val createdAt: String? = null
)
