package com.fauzangifari.kostrack.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import com.fauzangifari.kostrack.ui.theme.Grey500
import com.fauzangifari.kostrack.ui.theme.NeoShadowDark
import com.fauzangifari.kostrack.ui.theme.NeoBg
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val user by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ProfileEvent.LogoutSuccess -> onLogoutSuccess()
                is ProfileEvent.Error -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = { KosTrackTopAppBar(title = "Profil") },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar with neo shadow
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .shadow(10.dp, CircleShape, ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                    .clip(CircleShape)
                    .background(NeoBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    tint = Green500,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.fullName ?: "Loading...",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )

            Text(
                text = user?.email ?: "",
                fontFamily = PlusJakartaSans,
                color = Grey500,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Menu Items
            ProfileMenuItem(
                icon = Icons.Rounded.Person,
                label = "Edit Profil",
                onClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileMenuItem(
                icon = Icons.Rounded.Settings,
                label = "Pengaturan",
                onClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            Button(
                onClick = viewModel::logout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE),
                    contentColor = Color(0xFFD32F2F)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Rounded.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Keluar Aplikasi",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Green500.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Green500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = label,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = Grey500
                )
            }
        }
    }
}
