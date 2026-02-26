package com.fauzangifari.kostrack.data.repository

import com.fauzangifari.kostrack.data.mapper.toDomain
import com.fauzangifari.kostrack.data.mapper.toDto
import com.fauzangifari.kostrack.data.model.dto.PaymentDto
import com.fauzangifari.kostrack.domain.model.Payment
import com.fauzangifari.kostrack.domain.repository.PaymentRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PaymentRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : PaymentRepository {

    override fun getPaymentsByProperty(propertyId: String): Flow<List<Payment>> = flow {
        val result = supabaseClient.postgrest["payments"]
            .select {
                filter { eq("property_id", propertyId) }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeList<PaymentDto>()
        emit(result.map { it.toDomain() })
    }.catch { emit(emptyList()) }

    override fun getPaymentsByTenant(tenantId: String): Flow<List<Payment>> = flow {
        val result = supabaseClient.postgrest["payments"]
            .select {
                filter { eq("tenant_id", tenantId) }
                order("due_date", order = Order.DESCENDING)
            }
            .decodeList<PaymentDto>()
        emit(result.map { it.toDomain() })
    }.catch { emit(emptyList()) }

    override suspend fun getPaymentById(id: String): Result<Payment> {
        return try {
            val result = supabaseClient.postgrest["payments"]
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle<PaymentDto>()
            Result.success(result.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPayment(payment: Payment): Result<Unit> {
        return try {
            supabaseClient.postgrest["payments"].insert(payment.toDto())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePayment(payment: Payment): Result<Unit> {
        return try {
            supabaseClient.postgrest["payments"].update(payment.toDto()) {
                filter { eq("id", payment.id) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePayment(id: String): Result<Unit> {
        return try {
            supabaseClient.postgrest["payments"].delete {
                filter { eq("id", id) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
