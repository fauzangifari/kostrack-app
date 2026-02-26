package com.fauzangifari.kostrack.domain.repository

import com.fauzangifari.kostrack.domain.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getRoomsByProperty(propertyId: String): Flow<List<Room>>
    suspend fun getRoomById(id: String): Result<Room>
    suspend fun createRoom(room: Room): Result<Unit>
    suspend fun updateRoom(room: Room): Result<Unit>
    suspend fun deleteRoom(id: String): Result<Unit>
}
