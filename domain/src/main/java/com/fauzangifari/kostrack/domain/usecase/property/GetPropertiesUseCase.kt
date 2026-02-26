package com.fauzangifari.kostrack.domain.usecase.property

import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.repository.AuthRepository
import com.fauzangifari.kostrack.domain.repository.PropertyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

class GetPropertiesUseCase(
    private val propertyRepository: PropertyRepository,
    private val authRepository: AuthRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Property>> {
        return authRepository.getCurrentUser()
            .filterNotNull()
            .flatMapLatest { user ->
                propertyRepository.getPropertiesByOwner(user.id)
            }
    }
}
