package com.fauzangifari.kostrack.data.repository

import com.fauzangifari.kostrack.data.mapper.toDomain
import com.fauzangifari.kostrack.data.mapper.toDto
import com.fauzangifari.kostrack.data.model.dto.PropertyDto
import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.repository.PropertyRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PropertyRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : PropertyRepository {

    override fun getPropertiesByOwner(ownerId: String): Flow<List<Property>> = flow {
        val result = supabaseClient.postgrest["properties"]
            .select {
                filter {
                    eq("owner_id", ownerId)
                }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeList<PropertyDto>()
        emit(result.map { it.toDomain() })
    }.catch { emit(emptyList()) }

    override suspend fun getPropertyById(id: String): Result<Property> {
        return try {
            val result = supabaseClient.postgrest["properties"]
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<PropertyDto>()
            Result.success(result.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createProperty(property: Property): Result<Unit> {
        return try {
            val dto = property.toDto()
            android.util.Log.d("PropertyRepo", "Inserting property DTO: $dto")
            supabaseClient.postgrest["properties"].insert(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("PropertyRepo", "Insert failed: ${e::class.simpleName} - ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProperty(property: Property): Result<Unit> {
        return try {
            supabaseClient.postgrest["properties"].update(property.toDto()) {
                filter {
                    eq("id", property.id)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProperty(id: String): Result<Unit> {
        return try {
            supabaseClient.postgrest["properties"].delete {
                filter {
                    eq("id", id)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
