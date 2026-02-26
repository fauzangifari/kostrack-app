package com.fauzangifari.kostrack.domain.usecase.room

import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.repository.RoomRepository

class SaveRoomUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(room: Room): Result<Unit> {
        if (room.roomNumber.isBlank()) return Result.failure(IllegalArgumentException("Nomor kamar tidak boleh kosong"))
        if (room.pricePerMonth <= 0) return Result.failure(IllegalArgumentException("Harga per bulan harus lebih dari 0"))
        if (room.propertyId.isBlank()) return Result.failure(IllegalArgumentException("Property ID tidak boleh kosong"))

        return if (room.id.isEmpty()) {
            roomRepository.createRoom(room)
        } else {
            roomRepository.updateRoom(room)
        }
    }
}
