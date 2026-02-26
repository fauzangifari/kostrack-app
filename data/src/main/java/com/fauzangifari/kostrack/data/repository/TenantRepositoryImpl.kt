package com.fauzangifari.kostrack.data.repository

import com.fauzangifari.kostrack.data.mapper.toDomain
import com.fauzangifari.kostrack.data.mapper.toDto
import com.fauzangifari.kostrack.data.model.dto.TenantDto
import com.fauzangifari.kostrack.domain.model.Tenant
import com.fauzangifari.kostrack.domain.repository.TenantRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TenantRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : TenantRepository {

    override fun getTenantsByProperty(propertyId: String): Flow<List<Tenant>> = flow {
        val result = supabaseClient.postgrest["tenants"]
            .select {
                filter { eq("property_id", propertyId) }
            }
            .decodeList<TenantDto>()
        emit(result.map { it.toDomain() })
    }.catch { emit(emptyList()) }

    override fun getTenantsByRoom(roomId: String): Flow<List<Tenant>> = flow {
        val result = supabaseClient.postgrest["tenants"]
            .select {
                filter { eq("room_id", roomId) }
            }
            .decodeList<TenantDto>()
        emit(result.map { it.toDomain() })
    }.catch { emit(emptyList()) }

    override suspend fun getTenantById(id: String): Result<Tenant> {
        return try {
            val result = supabaseClient.postgrest["tenants"]
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle<TenantDto>()
            Result.success(result.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTenant(tenant: Tenant): Result<Unit> {
        return try {
            supabaseClient.postgrest["tenants"].insert(tenant.toDto())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTenant(tenant: Tenant): Result<Unit> {
        return try {
            supabaseClient.postgrest["tenants"].update(tenant.toDto()) {
                filter { eq("id", tenant.id) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTenant(id: String): Result<Unit> {
        return try {
            supabaseClient.postgrest["tenants"].delete {
                filter { eq("id", id) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
