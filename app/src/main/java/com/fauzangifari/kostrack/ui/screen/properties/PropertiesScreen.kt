package com.fauzangifari.kostrack.ui.screen.properties

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.fauzangifari.kostrack.domain.model.PropertyType
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.theme.Green100
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.Green700
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import com.fauzangifari.kostrack.ui.theme.Grey500
import com.fauzangifari.kostrack.ui.theme.NeoShadowDark
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertiesScreen(
    onAddPropertyClick: () -> Unit,
    onPropertyClick: (String) -> Unit,
    viewModel: PropertyListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = { KosTrackTopAppBar(title = "Kos Saya") },
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPropertyClick,
                containerColor = Green500,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Properti")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is PropertyListState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Green500
                    )
                }
                is PropertyListState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.properties) { item ->
                            PropertyItem(
                                item = item,
                                onClick = { onPropertyClick(item.property.id) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
                is PropertyListState.Empty -> {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                }
                is PropertyListState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red,
                        fontFamily = PlusJakartaSans
                    )
                }
            }
        }
    }
}

@Composable
fun PropertyItem(
    item: PropertyWithRoomInfo,
    onClick: () -> Unit
) {
    val property = item.property
    val occupancyRate = if (item.totalRooms > 0) item.occupiedRooms.toFloat() / item.totalRooms else 0f
    val occupancyPercent = (occupancyRate * 100).toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(18.dp)
    ) {
        Column {
            // Header: Icon + Name + Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Green500.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Apartment,
                        contentDescription = null,
                        tint = Green500,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = property.name,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            tint = Grey500,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = property.address,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = Grey500
                        )
                    }
                }
                Surface(
                    color = when(property.type) {
                        PropertyType.PUTRA -> Color(0xFFE3F2FD)
                        PropertyType.PUTRI -> Color(0xFFFCE4EC)
                        PropertyType.CAMPUR -> Color(0xFFF1F8E9)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = property.type.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when(property.type) {
                            PropertyType.PUTRA -> Color(0xFF1976D2)
                            PropertyType.PUTRI -> Color(0xFFC2185B)
                            PropertyType.CAMPUR -> Green500
                        },
                        fontFamily = PlusJakartaSans
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar hunian
            LinearProgressIndicator(
                progress = { occupancyRate },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = when {
                    occupancyPercent >= 80 -> Green500
                    occupancyPercent >= 50 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                },
                trackColor = Green100.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Info bawah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (item.totalRooms > 0) "${item.occupiedRooms}/${item.totalRooms} kamar terisi" else "Belum ada kamar",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = Grey500
                )
                Text(
                    text = if (item.totalRooms > 0) "$occupancyPercent%" else "",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        occupancyPercent >= 80 -> Green500
                        occupancyPercent >= 50 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Apartment,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Grey500.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Belum ada kos terdaftar",
            fontFamily = PlusJakartaSans,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Grey500
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Klik tombol + untuk menambah kos baru",
            fontFamily = PlusJakartaSans,
            fontSize = 14.sp,
            color = Grey500.copy(alpha = 0.7f)
        )
    }
}