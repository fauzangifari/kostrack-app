package com.fauzangifari.kostrack.domain.usecase.property

import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.repository.PropertyRepository

class SavePropertyUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(property: Property): Result<Unit> {
        if (property.name.isBlank()) return Result.failure(IllegalArgumentException("Nama properti tidak boleh kosong"))
        if (property.address.isBlank()) return Result.failure(IllegalArgumentException("Alamat tidak boleh kosong"))

        return if (property.id.isEmpty()) {
            propertyRepository.createProperty(property)
        } else {
            propertyRepository.updateProperty(property)
        }
    }
}
