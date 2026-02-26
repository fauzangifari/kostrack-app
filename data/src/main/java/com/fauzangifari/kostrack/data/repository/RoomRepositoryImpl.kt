package com.fauzangifari.kostrack.data.repository

import com.fauzangifari.kostrack.data.mapper.toDomain
import com.fauzangifari.kostrack.data.mapper.toDto
import com.fauzangifari.kostrack.data.model.dto.RoomDto
import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.repository.RoomRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RoomRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : RoomRepository {

    override fun getRoomsByProperty(propertyId: String): Flow<List<Room>> = flow {
        val result = supabaseClient.postgrest["rooms"]
            .select {
                filter {
                    eq("property_id", propertyId)
                }
            }
            .decodeList<RoomDto>()
        emit(result.map { it.toDomain() })
    }.catch { emit(emptyList()) }

    override suspend fun getRoomById(id: String): Result<Room> {
        return try {
            val result = supabaseClient.postgrest["rooms"]
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<RoomDto>()
            Result.success(result.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRoom(room: Room): Result<Unit> {
        return try {
            supabaseClient.postgrest["rooms"].insert(room.toDto())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateRoom(room: Room): Result<Unit> {
        return try {
            supabaseClient.postgrest["rooms"].update(room.toDto()) {
                filter {
                    eq("id", room.id)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteRoom(id: String): Result<Unit> {
        return try {
            supabaseClient.postgrest["rooms"].delete {
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
