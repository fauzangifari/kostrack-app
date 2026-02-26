package com.fauzangifari.kostrack.domain.usecase.tenant

import com.fauzangifari.kostrack.domain.model.RoomStatus
import com.fauzangifari.kostrack.domain.repository.RoomRepository
import com.fauzangifari.kostrack.domain.repository.TenantRepository

class RemoveTenantUseCase(
    private val tenantRepository: TenantRepository,
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(tenantId: String, roomId: String): Result<Unit> {
        val deleteResult = tenantRepository.deleteTenant(tenantId)
        if (deleteResult.isSuccess) {
            val roomResult = roomRepository.getRoomById(roomId)
            roomResult.onSuccess { room ->
                roomRepository.updateRoom(room.copy(status = RoomStatus.AVAILABLE))
            }
        }
        return deleteResult
    }
}
