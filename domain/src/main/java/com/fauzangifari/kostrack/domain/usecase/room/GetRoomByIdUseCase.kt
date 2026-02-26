package com.fauzangifari.kostrack.domain.usecase.room

import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.repository.RoomRepository

class GetRoomByIdUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(id: String): Result<Room> {
        if (id.isBlank()) return Result.failure(IllegalArgumentException("Room ID tidak boleh kosong"))
        return roomRepository.getRoomById(id)
    }
}
