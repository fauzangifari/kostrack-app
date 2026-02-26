package com.fauzangifari.kostrack.ui.screen.tenants

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.ui.components.ButtonCustom
import com.fauzangifari.kostrack.ui.components.ButtonStyle
import com.fauzangifari.kostrack.ui.components.ButtonType
import com.fauzangifari.kostrack.ui.components.GlassCard
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.components.TextInput
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Composable
fun TenantEditorScreen(
    propertyId: String,
    roomId: String,
    onBackClick: () -> Unit,
    viewModel: TenantEditorViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(propertyId, roomId) {
        viewModel.initEditor(propertyId, roomId)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is TenantEditorEvent.Success -> {
                    Toast.makeText(context, "Penghuni berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
                is TenantEditorEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            KosTrackTopAppBar(title = "Tambah Penghuni", onBackClick = onBackClick)
        },
        containerColor = Color.White
    ) { innerPadding ->
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
                        label = "Nama Lengkap",
                        value = uiState.fullName,
                        onValueChange = viewModel::onFullNameChange,
                        placeholder = "Masukkan nama penghuni"
                    )

                    TextInput(
                        label = "No. HP",
                        value = uiState.phone,
                        onValueChange = viewModel::onPhoneChange,
                        placeholder = "Contoh: 08123456789"
                    )

                    DateInputField(
                        label = "Tanggal Mulai Sewa",
                        value = uiState.startDate,
                        onValueChange = viewModel::onStartDateChange,
                        placeholder = "Pilih tanggal mulai"
                    )

                    DateInputField(
                        label = "Tanggal Selesai (Opsional)",
                        value = uiState.endDate,
                        onValueChange = viewModel::onEndDateChange,
                        placeholder = "Pilih tanggal selesai"
                    )
                }
            }

            ButtonCustom(
                value = "Simpan Penghuni",
                onClick = viewModel::assignTenant,
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

@Composable
fun DateInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    color = Color.Gray.copy(alpha = 0.6f),
                    fontFamily = PlusJakartaSans
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Rounded.CalendarMonth,
                    contentDescription = "Pilih tanggal",
                    tint = Green500
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            onValueChange(String.format("%04d-%02d-%02d", year, month + 1, day))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xFF034125).copy(alpha = 0.5f),
                disabledTextColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledTrailingIconColor = Green500,
                disabledPlaceholderColor = Color.Gray.copy(alpha = 0.6f)
            )
        )
    }
}
