package com.fauzangifari.kostrack.domain.usecase.room

import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetRoomsByPropertyUseCase(
    private val roomRepository: RoomRepository
) {
    operator fun invoke(propertyId: String): Flow<List<Room>> {
        return roomRepository.getRoomsByProperty(propertyId)
    }
}
