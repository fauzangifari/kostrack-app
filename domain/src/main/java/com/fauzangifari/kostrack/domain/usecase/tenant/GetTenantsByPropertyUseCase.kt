package com.fauzangifari.kostrack.domain.usecase.tenant

import com.fauzangifari.kostrack.domain.model.Tenant
import com.fauzangifari.kostrack.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow

class GetTenantsByPropertyUseCase(
    private val tenantRepository: TenantRepository
) {
    operator fun invoke(propertyId: String): Flow<List<Tenant>> {
        return tenantRepository.getTenantsByProperty(propertyId)
    }
}
