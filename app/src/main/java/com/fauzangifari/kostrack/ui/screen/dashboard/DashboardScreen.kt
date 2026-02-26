package com.fauzangifari.kostrack.ui.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fauzangifari.kostrack.ui.theme.Green100
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.Green700
import com.fauzangifari.kostrack.ui.theme.Grey500
import com.fauzangifari.kostrack.ui.theme.NeoShadowDark
import com.fauzangifari.kostrack.ui.theme.NeoBg
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    onProfileClick: () -> Unit = {},
    onAddPropertyClick: () -> Unit = {},
    onPropertyClick: (String) -> Unit = {},
    onViewAllPropertiesClick: () -> Unit = {},
    onTransactionsClick: () -> Unit = {}
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()
    val userName = user?.fullName ?: "User"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        TopBarDashboard(
            userName = userName,
            onProfileClick = onProfileClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
        // Overview Stats
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Ringkasan Bisnis",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Green700
                )
                Spacer(modifier = Modifier.height(10.dp))
                OverviewStats(
                    occupiedRooms = state.occupiedRooms,
                    totalRooms = state.totalRooms,
                    totalTenants = state.totalTenants,
                    totalProperties = state.propertySummaries.size
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Quick Actions
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Aksi Cepat",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Green700
                )
                Spacer(modifier = Modifier.height(10.dp))
                QuickActionsRow(
                    onAddPropertyClick = onAddPropertyClick,
                    onTransactionsClick = onTransactionsClick
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Property Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Properti Saya",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Green700
                )
                TextButton(onClick = onViewAllPropertiesClick) {
                    Text("Lihat Semua", color = Green500, fontFamily = PlusJakartaSans, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Green500, modifier = Modifier.size(32.dp))
                }
            }
        } else if (state.propertySummaries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Rounded.Business,
                            contentDescription = null,
                            tint = Grey500,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Belum ada properti",
                            fontFamily = PlusJakartaSans,
                            color = Grey500
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onAddPropertyClick) {
                            Text("+ Tambah Properti", color = Green500, fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(state.propertySummaries.take(3)) { summary ->
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    PropertyCard(
                        name = summary.property.name,
                        address = summary.property.address,
                        occupiedRooms = summary.occupiedRooms,
                        totalRooms = summary.totalRooms,
                        onClick = { onPropertyClick(summary.property.id) }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
    }
}

@Composable
fun TopBarDashboard(
    userName: String,
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Selamat Datang 👋",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                color = Grey500
            )
            Text(
                text = userName,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Green700
            )
        }
        IconButton(onClick = onProfileClick) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Green100.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile",
                    tint = Green500,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun OverviewStats(
    occupiedRooms: Int,
    totalRooms: Int,
    totalTenants: Int,
    totalProperties: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GradientStatCard(
                label = "Kamar Terisi",
                value = "$occupiedRooms/$totalRooms",
                icon = Icons.Rounded.DoorSliding,
                gradient = Brush.linearGradient(listOf(Green500, Green700)),
                modifier = Modifier.weight(1f)
            )
            GradientStatCard(
                label = "Penghuni",
                value = "$totalTenants",
                icon = Icons.Rounded.People,
                gradient = Brush.linearGradient(listOf(Color(0xFF42A5F5), Color(0xFF1565C0))),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GradientStatCard(
                label = "Properti",
                value = "$totalProperties",
                icon = Icons.Rounded.Business,
                gradient = Brush.linearGradient(listOf(Color(0xFFAB47BC), Color(0xFF7B1FA2))),
                modifier = Modifier.weight(1f)
            )
            GradientStatCard(
                label = "Kamar Kosong",
                value = "${totalRooms - occupiedRooms}",
                icon = Icons.Rounded.MeetingRoom,
                gradient = Brush.linearGradient(listOf(Color(0xFFFF7043), Color(0xFFD84315))),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun GradientStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    gradient: Brush,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1.6f)
            .shadow(6.dp, RoundedCornerShape(18.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
            .clip(RoundedCornerShape(18.dp))
            .background(gradient)
            .padding(14.dp)
    ) {
        // Icon besar sebagai background
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.12f),
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterEnd)
        )
        // Content di atas
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
fun QuickActionsRow(
    onAddPropertyClick: () -> Unit = {},
    onTransactionsClick: () -> Unit = {}
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            QuickActionItem(
                icon = Icons.Rounded.AddBusiness,
                label = "Kos Baru",
                accentColor = Green500,
                onClick = onAddPropertyClick
            )
        }
        item {
            QuickActionItem(
                icon = Icons.Rounded.Receipt,
                label = "Transaksi",
                accentColor = Color(0xFFFF9800),
                onClick = onTransactionsClick
            )
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                .clip(RoundedCornerShape(16.dp))
                .background(NeoBg)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PropertyCard(
    name: String,
    address: String,
    occupiedRooms: Int,
    totalRooms: Int,
    onClick: () -> Unit = {}
) {
    val occupancyRate = if (totalRooms > 0) occupiedRooms.toFloat() / totalRooms else 0f
    val occupancyPercent = (occupancyRate * 100).toInt()
    val accentColor = when {
        occupancyPercent >= 80 -> Green500
        occupancyPercent >= 50 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Row {
            // Accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(90.dp)
                    .background(accentColor)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Business,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.LocationOn,
                            contentDescription = null,
                            tint = Grey500,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = address,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            color = Grey500
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { occupancyRate },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(CircleShape),
                        color = accentColor,
                        trackColor = Green100.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (totalRooms > 0) "$occupancyPercent% terisi ($occupiedRooms/$totalRooms kamar)" else "Belum ada kamar",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        color = Grey500
                    )
                }
            }
        }
    }
}