package com.fauzangifari.kostrack.domain.usecase.payment

import com.fauzangifari.kostrack.domain.model.Payment
import com.fauzangifari.kostrack.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow

class GetPaymentsByTenantUseCase(
    private val paymentRepository: PaymentRepository
) {
    operator fun invoke(tenantId: String): Flow<List<Payment>> {
        return paymentRepository.getPaymentsByTenant(tenantId)
    }
}
