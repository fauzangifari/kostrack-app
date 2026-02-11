package com.fauzangifari.kostrack.ui.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    onProfileClick: () -> Unit = {},
    onAddPropertyClick: () -> Unit = {}
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val userName = user?.fullName ?: "User"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopBarDashboard(
                userName = userName,
                onProfileClick = onProfileClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Text(
                    text = "Ringkasan Bisnis",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                OverviewStats()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = "Aksi Cepat",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuickActionsRow()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Properti Saya",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    TextButton(onClick = onAddPropertyClick) {
                        Text("Lihat Semua", color = Green500, fontFamily = PlusJakartaSans)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(2) {
                PropertyCard()
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun TopBarDashboard(
    userName: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Selamat Pagi,",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = userName,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = Color.Black
            )
        }
        
        // Profile Avatar placeholder
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Green500.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Profile",
                tint = Green500
            )
        }
    }
}

@Composable
fun OverviewStats() {
    Row(modifier = Modifier.fillMaxWidth()) {
        StatCard(
            label = "Pendapatan",
            value = "Rp 12.5jt",
            icon = Icons.Rounded.Payments,
            containerColor = Color(0xFFE8F5E9),
            contentColor = Color(0xFF2E7D32),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        StatCard(
            label = "Kamar Terisi",
            value = "18/20",
            icon = Icons.Rounded.DoorSliding,
            containerColor = Color(0xFFFFF3E0),
            contentColor = Color(0xFFE65100),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(containerColor)
            .padding(20.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = label, fontFamily = PlusJakartaSans, fontSize = 12.sp, color = contentColor.copy(alpha = 0.7f))
        Text(text = value, fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = contentColor)
    }
}

@Composable
fun QuickActionsRow() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        item { QuickActionItem(Icons.Rounded.AddBusiness, "Kos Baru") }
        item { QuickActionItem(Icons.Rounded.GroupAdd, "Penyewa") }
        item { QuickActionItem(Icons.Rounded.Receipt, "Tagihan") }
        item { QuickActionItem(Icons.Rounded.Assessment, "Laporan") }
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = Color.Black)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontFamily = PlusJakartaSans, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun PropertyCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image Placeholder
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF1F3F4)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Business,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(32.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Kos Kemanggisan 1",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Jakarta Barat",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.8f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = Green500,
                trackColor = Color(0xFFE0E0E0)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopBarDashboard(
                userName = "Fauzan Gifari",
                onProfileClick = {}
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 24.dp)) {
            Text(
                text = "Ringkasan Bisnis",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            OverviewStats()
        }
    }
}