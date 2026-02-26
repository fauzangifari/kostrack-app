package com.fauzangifari.kostrack.ui.screen.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.domain.model.Payment
import com.fauzangifari.kostrack.domain.model.PaymentStatus
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.Grey500
import com.fauzangifari.kostrack.ui.theme.NeoShadowDark
import com.fauzangifari.kostrack.ui.theme.NeoBg
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionsScreen(viewModel: TransactionsViewModel = koinViewModel()) {
    val filteredPayments by viewModel.filteredPayments.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val propertyNames by viewModel.propertyNames.collectAsState()

    val tabs = listOf("Semua", "Lunas", "Belum Bayar")

    Scaffold(
        topBar = { KosTrackTopAppBar(title = "Transaksi") },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(Modifier.height(8.dp))

            // Tab Row
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeoBg)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, title ->
                    val selected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) Green500 else Color.Transparent)
                            .clickable { viewModel.selectTab(index) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title,
                            fontFamily = PlusJakartaSans,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selected) Color.White else Grey500
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (filteredPayments.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(6.dp, CircleShape, ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                            .clip(CircleShape)
                            .background(NeoBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.ReceiptLong,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = Grey500.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "Belum ada transaksi",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Transaksi pembayaran akan muncul di sini",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        color = Grey500
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(24.dp, 0.dp, 24.dp, 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPayments) { payment ->
                        TransactionCard(
                            payment = payment,
                            propertyName = propertyNames[payment.propertyId] ?: "-"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionCard(payment: Payment, propertyName: String) {
    val statusColor = when (payment.status) {
        PaymentStatus.PAID -> Color(0xFF4CAF50)
        PaymentStatus.PENDING -> Color(0xFFFFA726)
        PaymentStatus.OVERDUE -> Color(0xFFEF5350)
        PaymentStatus.CANCELLED -> Grey500
    }
    val statusLabel = when (payment.status) {
        PaymentStatus.PAID -> "Lunas"
        PaymentStatus.PENDING -> "Menunggu"
        PaymentStatus.OVERDUE -> "Terlambat"
        PaymentStatus.CANCELLED -> "Dibatalkan"
    }

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        statusLabel,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = statusColor
                    )
                }

                Text(
                    currencyFormat.format(payment.amount),
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Apartment, null, Modifier.size(16.dp), Grey500)
                Spacer(Modifier.width(6.dp))
                Text(
                    propertyName,
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    color = Grey500
                )
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.DateRange, null, Modifier.size(16.dp), Grey500)
                Spacer(Modifier.width(6.dp))
                Text(
                    "Jatuh tempo: ${payment.dueDate}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    color = Grey500
                )
            }

            if (payment.paidDate != null) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.CheckCircle, null, Modifier.size(16.dp), Color(0xFF4CAF50))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Dibayar: ${payment.paidDate}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            if (!payment.notes.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    payment.notes!!,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = Grey500.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
