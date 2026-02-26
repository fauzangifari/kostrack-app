package com.fauzangifari.kostrack.ui.screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Payment
import com.fauzangifari.kostrack.domain.model.PaymentStatus
import com.fauzangifari.kostrack.domain.usecase.payment.GetPaymentsByPropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.property.GetPropertiesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class TransactionsViewModel(
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getPaymentsByPropertyUseCase: GetPaymentsByPropertyUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _propertyNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val propertyNames: StateFlow<Map<String, String>> = _propertyNames.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val allPayments: StateFlow<List<Payment>> = getPropertiesUseCase()
        .onEach { properties ->
            _propertyNames.value = properties.associate { it.id to it.name }
        }
        .flatMapLatest { properties ->
            if (properties.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    properties.map { getPaymentsByPropertyUseCase(it.id) }
                ) { paymentLists ->
                    paymentLists.flatMap { it.toList() }.sortedByDescending { it.dueDate }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredPayments: StateFlow<List<Payment>> = combine(allPayments, _selectedTab) { payments, tab ->
        when (tab) {
            1 -> payments.filter { it.status == PaymentStatus.PAID }
            2 -> payments.filter { it.status != PaymentStatus.PAID }
            else -> payments
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }
}
