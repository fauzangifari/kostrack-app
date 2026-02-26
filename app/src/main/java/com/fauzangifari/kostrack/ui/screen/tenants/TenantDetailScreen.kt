package com.fauzangifari.kostrack.ui.screen.tenants

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.ui.components.ButtonCustom
import com.fauzangifari.kostrack.ui.components.ButtonStyle
import com.fauzangifari.kostrack.ui.components.ButtonType
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun TenantDetailScreen(
    tenantId: String,
    onBackClick: () -> Unit,
    viewModel: TenantDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showRemoveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(tenantId) {
        viewModel.loadTenant(tenantId)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is TenantDetailEvent.RemoveSuccess -> {
                    Toast.makeText(context, "Penghuni berhasil dihapus", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
                is TenantDetailEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = {
                Text(
                    "Hapus Penghuni",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Apakah kamu yakin ingin menghapus ${uiState.tenant?.fullName}? Status kamar akan berubah menjadi Tersedia.",
                    fontFamily = PlusJakartaSans
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRemoveDialog = false
                        viewModel.removeTenant()
                    }
                ) {
                    Text("Hapus", color = ErrorBase, fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Batal", color = Green700, fontFamily = PlusJakartaSans)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            KosTrackTopAppBar(title = "Detail Penghuni", onBackClick = onBackClick)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading && uiState.tenant == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Green500)
                }
            } else if (uiState.tenant != null) {
                val tenant = uiState.tenant!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Avatar & Name
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(20.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .shadow(6.dp, RoundedCornerShape(36.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                                    .clip(RoundedCornerShape(36.dp))
                                    .background(Green500.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = Green700,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = tenant.fullName,
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Surface(
                                color = if (tenant.isActive) SuccessLight else ErrorLight,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(
                                    text = if (tenant.isActive) "Aktif" else "Tidak Aktif",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = PlusJakartaSans,
                                    color = if (tenant.isActive) SuccessBase else ErrorBase
                                )
                            }
                        }
                    }

                    // Info Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(20.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                "Informasi",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Green700
                            )

                            TenantInfoRow(
                                icon = Icons.Rounded.Phone,
                                label = "No. HP",
                                value = tenant.phone ?: "-"
                            )

                            TenantInfoRow(
                                icon = Icons.Rounded.CalendarMonth,
                                label = "Tanggal Mulai",
                                value = tenant.startDate
                            )

                            TenantInfoRow(
                                icon = Icons.Rounded.CalendarMonth,
                                label = "Tanggal Selesai",
                                value = tenant.endDate ?: "-"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Remove button
                    ButtonCustom(
                        value = "Hapus Penghuni",
                        onClick = { showRemoveDialog = true },
                        isEnabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        fontSize = 16,
                        buttonType = ButtonType.PILL,
                        buttonStyle = ButtonStyle.OUTLINED,
                        borderColor = ErrorBase,
                        textColor = ErrorBase
                    )

                    if (uiState.isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Green500
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Gagal memuat data penghuni",
                        fontFamily = PlusJakartaSans,
                        color = Grey500
                    )
                }
            }
        }
    }
}

@Composable
private fun TenantInfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Green500, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Grey500,
                fontFamily = PlusJakartaSans
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontFamily = PlusJakartaSans
            )
        }
    }
}
