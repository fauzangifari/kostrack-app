package com.fauzangifari.kostrack.ui.screen.rooms

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.domain.model.RoomStatus
import com.fauzangifari.kostrack.ui.components.ButtonCustom
import com.fauzangifari.kostrack.ui.components.ButtonStyle
import com.fauzangifari.kostrack.ui.components.ButtonType
import com.fauzangifari.kostrack.ui.components.GlassCard
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.components.TextInput
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import org.koin.androidx.compose.koinViewModel

private val DEFAULT_FACILITIES = listOf(
    "WiFi", "AC", "Kamar Mandi Dalam", "Kasur", "Lemari",
    "Meja", "Kursi", "Kipas Angin", "TV", "Dapur Bersama"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoomEditorScreen(
    propertyId: String,
    roomId: String? = null,
    onBackClick: () -> Unit,
    viewModel: RoomEditorViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var customFacility by remember { mutableStateOf("") }

    LaunchedEffect(propertyId, roomId) {
        viewModel.initEditor(propertyId, roomId)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is RoomEditorEvent.Success -> {
                    Toast.makeText(context, "Berhasil menyimpan kamar", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }

                is RoomEditorEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            KosTrackTopAppBar(
                title = if (uiState.isEditMode) "Edit Kamar" else "Tambah Kamar",
                onBackClick = onBackClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        if (uiState.isLoading && uiState.isEditMode && uiState.roomNumber.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Green500)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                GlassCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        TextInput(
                            label = "Nomor Kamar",
                            value = uiState.roomNumber,
                            onValueChange = viewModel::onRoomNumberChange,
                            placeholder = "Contoh: A1, 101, dsb"
                        )

                        TextInput(
                            label = "Harga per Bulan (Rp)",
                            value = uiState.pricePerMonth,
                            onValueChange = viewModel::onPriceChange,
                            placeholder = "Contoh: 1500000"
                        )

                        Text(
                            text = "Status Kamar",
                            fontSize = 14.sp,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RoomStatus.entries.forEach { status ->
                                FilterChip(
                                    selected = uiState.status == status,
                                    onClick = { viewModel.onStatusChange(status) },
                                    label = {
                                        Text(
                                            when (status) {
                                                RoomStatus.AVAILABLE -> "Tersedia"
                                                RoomStatus.OCCUPIED -> "Terisi"
                                                RoomStatus.MAINTENANCE -> "Perbaikan"
                                            }
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Green500,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                // Facilities section
                GlassCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Fasilitas Kamar",
                            fontSize = 14.sp,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        // Default facilities checklist
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            DEFAULT_FACILITIES.forEach { facility ->
                                FilterChip(
                                    selected = uiState.facilities.contains(facility),
                                    onClick = { viewModel.toggleFacility(facility) },
                                    label = {
                                        Text(
                                            facility,
                                            fontSize = 13.sp,
                                            fontFamily = PlusJakartaSans
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Green500,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        // Custom facilities (non-default ones)
                        val customFacilities =
                            uiState.facilities.filter { it !in DEFAULT_FACILITIES }
                        if (customFacilities.isNotEmpty()) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                customFacilities.forEach { facility ->
                                    FilterChip(
                                        selected = true,
                                        onClick = { viewModel.toggleFacility(facility) },
                                        label = {
                                            Text(
                                                facility,
                                                fontSize = 13.sp,
                                                fontFamily = PlusJakartaSans
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Green500,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        // Add custom facility input
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customFacility,
                                onValueChange = { customFacility = it },
                                placeholder = {
                                    Text(
                                        "Tambah fasilitas lain...",
                                        fontSize = 14.sp,
                                        color = Color.Gray.copy(alpha = 0.6f),
                                        fontFamily = PlusJakartaSans
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = PlusJakartaSans
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green500,
                                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )
                            IconButton(
                                onClick = {
                                    viewModel.addCustomFacility(customFacility.trim())
                                    customFacility = ""
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Green500, RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    Icons.Rounded.Add,
                                    contentDescription = "Tambah",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                ButtonCustom(
                    value = if (uiState.isEditMode) "Perbarui Kamar" else "Simpan Kamar",
                    onClick = viewModel::saveRoom,
                    isEnabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    fontSize = 16,
                    buttonType = ButtonType.PILL,
                    buttonStyle = ButtonStyle.FILLED
                )

                if (uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Green500
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
