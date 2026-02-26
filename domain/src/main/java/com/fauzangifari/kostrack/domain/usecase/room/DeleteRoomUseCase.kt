package com.fauzangifari.kostrack.domain.usecase.room

import com.fauzangifari.kostrack.domain.repository.RoomRepository

class DeleteRoomUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        if (id.isBlank()) return Result.failure(IllegalArgumentException("Room ID tidak boleh kosong"))
        return roomRepository.deleteRoom(id)
    }
}
