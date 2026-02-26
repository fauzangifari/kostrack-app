package com.fauzangifari.kostrack.domain.usecase.payment

import com.fauzangifari.kostrack.domain.model.Payment
import com.fauzangifari.kostrack.domain.model.PaymentStatus
import com.fauzangifari.kostrack.domain.repository.PaymentRepository

class RecordPaymentUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(payment: Payment): Result<Unit> {
        if (payment.amount <= 0) return Result.failure(IllegalArgumentException("Jumlah pembayaran harus lebih dari 0"))
        if (payment.tenantId.isBlank()) return Result.failure(IllegalArgumentException("Tenant ID tidak boleh kosong"))
        if (payment.dueDate.isBlank()) return Result.failure(IllegalArgumentException("Tanggal jatuh tempo tidak boleh kosong"))

        return if (payment.id.isEmpty()) {
            paymentRepository.createPayment(payment)
        } else {
            paymentRepository.updatePayment(payment)
        }
    }
}
