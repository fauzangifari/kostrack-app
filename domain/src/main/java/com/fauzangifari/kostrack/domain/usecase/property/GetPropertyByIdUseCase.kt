package com.fauzangifari.kostrack.domain.usecase.property

import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.repository.PropertyRepository

class GetPropertyByIdUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(id: String): Result<Property> {
        if (id.isBlank()) return Result.failure(IllegalArgumentException("Property ID tidak boleh kosong"))
        return propertyRepository.getPropertyById(id)
    }
}
