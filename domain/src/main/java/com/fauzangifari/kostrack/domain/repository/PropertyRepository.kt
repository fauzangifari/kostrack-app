package com.fauzangifari.kostrack.domain.repository

import com.fauzangifari.kostrack.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    fun getPropertiesByOwner(ownerId: String): Flow<List<Property>>
    suspend fun getPropertyById(id: String): Result<Property>
    suspend fun createProperty(property: Property): Result<Unit>
    suspend fun updateProperty(property: Property): Result<Unit>
    suspend fun deleteProperty(id: String): Result<Unit>
}
