package com.fauzangifari.kostrack.domain.usecase.payment

import com.fauzangifari.kostrack.domain.model.Payment
import com.fauzangifari.kostrack.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow

class GetPaymentsByPropertyUseCase(
    private val paymentRepository: PaymentRepository
) {
    operator fun invoke(propertyId: String): Flow<List<Payment>> {
        return paymentRepository.getPaymentsByProperty(propertyId)
    }
}
