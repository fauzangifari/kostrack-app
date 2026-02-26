package com.fauzangifari.kostrack.ui.screen.properties

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.domain.model.PropertyType
import com.fauzangifari.kostrack.ui.components.ButtonCustom
import com.fauzangifari.kostrack.ui.components.ButtonStyle
import com.fauzangifari.kostrack.ui.components.ButtonType
import com.fauzangifari.kostrack.ui.components.GlassCard
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.components.TextInput
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyEditorScreen(
    propertyId: String? = null,
    onBackClick: () -> Unit,
    viewModel: PropertyEditorViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(propertyId) {
        viewModel.loadProperty(propertyId)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is PropertyEditorEvent.Success -> {
                    Toast.makeText(context, "Berhasil menyimpan properti", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
                is PropertyEditorEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            KosTrackTopAppBar(
                title = if (uiState.isEditMode) "Edit Kos" else "Tambah Kos",
                onBackClick = onBackClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        if (uiState.isLoading && uiState.isEditMode && uiState.name.isEmpty()) {
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
                            label = "Nama Kos",
                            value = uiState.name,
                            onValueChange = viewModel::onNameChange,
                            placeholder = "Contoh: Kost Amanah Putri",
                            errorText = uiState.nameError
                        )

                        TextInput(
                            label = "Alamat Lengkap",
                            value = uiState.address,
                            onValueChange = viewModel::onAddressChange,
                            placeholder = "Jl. Merdeka No. 123, Jakarta",
                            errorText = uiState.addressError
                        )

                        Text(
                            text = "Tipe Kos",
                            fontSize = 14.sp,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PropertyType.entries.forEach { type ->
                                FilterChip(
                                    selected = uiState.type == type,
                                    onClick = { viewModel.onTypeChange(type) },
                                    label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Green500,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        TextInput(
                            label = "Deskripsi (Opsional)",
                            value = uiState.description,
                            onValueChange = viewModel::onDescriptionChange,
                            placeholder = "Fasilitas umum, peraturan, dll",
                            singleLine = false
                        )

                        TextInput(
                            label = "Link Foto (Opsional)",
                            value = uiState.imageUrl,
                            onValueChange = viewModel::onImageUrlChange,
                            placeholder = "https://example.com/foto.jpg"
                        )
                    }
                }

                ButtonCustom(
                    value = if (uiState.isEditMode) "Perbarui Properti" else "Simpan Properti",
                    onClick = viewModel::saveProperty,
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
