package com.fauzangifari.kostrack.domain.repository

import com.fauzangifari.kostrack.domain.model.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun getPaymentsByProperty(propertyId: String): Flow<List<Payment>>
    fun getPaymentsByTenant(tenantId: String): Flow<List<Payment>>
    suspend fun getPaymentById(id: String): Result<Payment>
    suspend fun createPayment(payment: Payment): Result<Unit>
    suspend fun updatePayment(payment: Payment): Result<Unit>
    suspend fun deletePayment(id: String): Result<Unit>
}
