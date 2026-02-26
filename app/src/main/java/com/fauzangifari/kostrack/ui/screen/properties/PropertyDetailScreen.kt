package com.fauzangifari.kostrack.ui.screen.properties

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.domain.model.PropertyType
import com.fauzangifari.kostrack.domain.model.Room
import com.fauzangifari.kostrack.domain.model.RoomStatus
import com.fauzangifari.kostrack.domain.model.Tenant
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.Green700
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import com.fauzangifari.kostrack.ui.theme.Grey500
import com.fauzangifari.kostrack.ui.theme.NeoShadowDark
import com.fauzangifari.kostrack.ui.theme.SuccessBase
import com.fauzangifari.kostrack.ui.theme.SuccessLight
import com.fauzangifari.kostrack.ui.theme.ErrorBase
import com.fauzangifari.kostrack.ui.theme.ErrorLight
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyDetailScreen(
    propertyId: String,
    onBackClick: () -> Unit,
    onEditPropertyClick: (String) -> Unit,
    onAddRoomClick: (String) -> Unit,
    onRoomClick: (String, String) -> Unit,
    onAssignTenantClick: (String, String) -> Unit = { _, _ -> },
    onTenantClick: (String) -> Unit = {},
    viewModel: PropertyDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Kamar", "Penghuni")

    LaunchedEffect(propertyId) {
        viewModel.loadDetail(propertyId)
    }

    Scaffold(
        topBar = {
            KosTrackTopAppBar(
                title = "Detail Kos",
                onBackClick = onBackClick
            ) {
                IconButton(onClick = { onEditPropertyClick(propertyId) }) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = Green500)
                }
            }
        },
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) {
                        onAddRoomClick(propertyId)
                    }
                },
                containerColor = Green500,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = if (selectedTab == 0) "Tambah Kamar" else "Tambah Penghuni"
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading && uiState.property == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Green500)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    uiState.property?.let { property ->
                        PropertyInfoCard(
                            name = property.name,
                            address = property.address,
                            type = property.type
                        )
                    }
                }

                item {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.White,
                        contentColor = Green700,
                        indicator = { tabPositions ->
                            @Suppress("DEPRECATION")
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = Green500
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val count = if (index == 0) uiState.rooms.size else uiState.tenants.filter { it.isActive }.size
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = "$title ($count)",
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }

                when (selectedTab) {
                    0 -> {
                        // Kamar tab
                        if (uiState.rooms.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Belum ada kamar terdaftar",
                                        fontFamily = PlusJakartaSans,
                                        color = Grey500
                                    )
                                }
                            }
                        }

                        items(uiState.rooms) { room ->
                            val tenantName = uiState.tenantByRoomId[room.id]?.fullName
                            RoomItem(
                                room = room,
                                tenantName = tenantName,
                                onClick = { onRoomClick(propertyId, room.id) },
                                onAssignClick = if (room.status == RoomStatus.AVAILABLE) {
                                    { onAssignTenantClick(propertyId, room.id) }
                                } else null
                            )
                        }
                    }

                    1 -> {
                        // Penghuni tab
                        val activeTenants = uiState.tenants.filter { it.isActive }
                        if (activeTenants.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Belum ada penghuni",
                                        fontFamily = PlusJakartaSans,
                                        color = Grey500
                                    )
                                }
                            }
                        }

                        items(activeTenants) { tenant ->
                            val room = uiState.rooms.find { it.id == tenant.roomId }
                            TenantItem(
                                tenant = tenant,
                                roomNumber = room?.roomNumber,
                                onClick = { onTenantClick(tenant.id) }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}


@Composable
fun PropertyInfoCard(name: String, address: String, type: PropertyType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = name,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = Grey500,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = address,
                    color = Grey500,
                    fontSize = 14.sp,
                    fontFamily = PlusJakartaSans
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = when (type) {
                    PropertyType.PUTRA -> Color(0xFFE3F2FD)
                    PropertyType.PUTRI -> Color(0xFFFCE4EC)
                    PropertyType.CAMPUR -> Color(0xFFF1F8E9)
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = type.name,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (type) {
                        PropertyType.PUTRA -> Color(0xFF1976D2)
                        PropertyType.PUTRI -> Color(0xFFC2185B)
                        PropertyType.CAMPUR -> Green500
                    },
                    fontFamily = PlusJakartaSans
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoomItem(
    room: Room,
    tenantName: String? = null,
    onClick: () -> Unit,
    onAssignClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Green500.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = room.roomNumber, fontWeight = FontWeight.Bold, color = Green700, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Rp ${String.format("%,.0f", room.pricePerMonth)}/bulan",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = when (room.status) {
                        RoomStatus.AVAILABLE -> Color(0xFFE8F5E9)
                        RoomStatus.OCCUPIED -> Color(0xFFE3F2FD)
                        RoomStatus.MAINTENANCE -> Color(0xFFFFF3E0)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = when (room.status) {
                            RoomStatus.AVAILABLE -> "Tersedia"
                            RoomStatus.OCCUPIED -> "Terisi"
                            RoomStatus.MAINTENANCE -> "Perbaikan"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (room.status) {
                            RoomStatus.AVAILABLE -> Green500
                            RoomStatus.OCCUPIED -> Color(0xFF1976D2)
                            RoomStatus.MAINTENANCE -> Color(0xFFFF9800)
                        },
                        fontFamily = PlusJakartaSans
                    )
                }
                if (tenantName != null && room.status == RoomStatus.OCCUPIED) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Person, contentDescription = null, tint = Grey500, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = tenantName, fontSize = 13.sp, color = Grey500, fontFamily = PlusJakartaSans)
                    }
                }
                if (room.facilities.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        room.facilities.take(4).forEach { facility ->
                            Surface(color = Green500.copy(alpha = 0.08f), shape = RoundedCornerShape(4.dp)) {
                                Text(
                                    text = facility,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp, color = Green700, fontFamily = PlusJakartaSans
                                )
                            }
                        }
                        if (room.facilities.size > 4) {
                            Text(text = "+${room.facilities.size - 4}", fontSize = 10.sp, color = Grey500, fontFamily = PlusJakartaSans)
                        }
                    }
                }
            }
            if (onAssignClick != null) {
                IconButton(onClick = onAssignClick) {
                    Icon(Icons.Rounded.PersonAdd, contentDescription = "Tambah Penghuni", tint = Green500, modifier = Modifier.size(20.dp))
                }
            } else {
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Grey500, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun TenantItem(
    tenant: Tenant,
    roomNumber: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Green500.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = Green700, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tenant.fullName,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                if (roomNumber != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Kamar $roomNumber",
                        fontSize = 13.sp,
                        color = Grey500,
                        fontFamily = PlusJakartaSans
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Mulai ${tenant.startDate}",
                    fontSize = 13.sp,
                    color = Grey500,
                    fontFamily = PlusJakartaSans
                )
            }
            Surface(
                color = if (tenant.isActive) SuccessLight else ErrorLight,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (tenant.isActive) "Aktif" else "Nonaktif",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PlusJakartaSans,
                    color = if (tenant.isActive) SuccessBase else ErrorBase
                )
            }
        }
    }
}
