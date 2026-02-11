package com.fauzangifari.kostrack.ui.screen.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans

@Composable
fun TransactionsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Catatan Transaksi & Tagihan", fontFamily = PlusJakartaSans)
    }
}
