package com.fauzangifari.kostrack.domain.repository

import com.fauzangifari.kostrack.domain.model.Tenant
import kotlinx.coroutines.flow.Flow

interface TenantRepository {
    fun getTenantsByProperty(propertyId: String): Flow<List<Tenant>>
    fun getTenantsByRoom(roomId: String): Flow<List<Tenant>>
    suspend fun getTenantById(id: String): Result<Tenant>
    suspend fun createTenant(tenant: Tenant): Result<Unit>
    suspend fun updateTenant(tenant: Tenant): Result<Unit>
    suspend fun deleteTenant(id: String): Result<Unit>
}
