package com.fauzangifari.kostrack.data.model.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PropertyDto(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("id") val id: String? = null,
    @SerialName("owner_id") val ownerId: String,
    @SerialName("name") val name: String,
    @SerialName("address") val address: String,
    @SerialName("description") val description: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("type") val type: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("created_at") val createdAt: String? = null
)
