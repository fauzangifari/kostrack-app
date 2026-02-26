package com.fauzangifari.kostrack.domain.usecase.property

import com.fauzangifari.kostrack.domain.repository.PropertyRepository

class DeletePropertyUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        if (id.isBlank()) return Result.failure(IllegalArgumentException("Property ID tidak boleh kosong"))
        return propertyRepository.deleteProperty(id)
    }
}
