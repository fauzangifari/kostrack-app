package com.fauzangifari.kostrack.domain.usecase.tenant

import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.model.RoomStatus
import com.fauzangifari.kostrack.domain.model.Tenant
import com.fauzangifari.kostrack.domain.repository.RoomRepository
import com.fauzangifari.kostrack.domain.repository.TenantRepository

class AssignTenantUseCase(
    private val tenantRepository: TenantRepository,
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(tenant: Tenant): Result<Unit> {
        if (tenant.fullName.isBlank()) return Result.failure(IllegalArgumentException("Nama tenant tidak boleh kosong"))
        if (tenant.roomId.isBlank()) return Result.failure(IllegalArgumentException("Room ID tidak boleh kosong"))
        if (tenant.startDate.isBlank()) return Result.failure(IllegalArgumentException("Tanggal mulai tidak boleh kosong"))

        val roomResult = roomRepository.getRoomById(tenant.roomId)
        val room = roomResult.getOrElse { return Result.failure(it) }

        if (room.status == RoomStatus.OCCUPIED) {
            return Result.failure(IllegalStateException("Kamar ${room.roomNumber} sudah terisi"))
        }
        if (room.status == RoomStatus.MAINTENANCE) {
            return Result.failure(IllegalStateException("Kamar ${room.roomNumber} sedang dalam perbaikan"))
        }

        val createResult = tenantRepository.createTenant(tenant)
        if (createResult.isSuccess) {
            roomRepository.updateRoom(room.copy(status = RoomStatus.OCCUPIED))
        }
        return createResult
    }
}
