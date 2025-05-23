package com.fauzangifari.kostrack.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.focus.onFocusChanged
import com.fauzangifari.kostrack.ui.theme.Green800
import com.fauzangifari.kostrack.ui.theme.Green900
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import com.fauzangifari.kostrack.R
import com.fauzangifari.kostrack.ui.theme.Grey500

@Composable
fun TextInput(
    label: String,
    value: String,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIconClick: (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(if (isFocused) Green800 else Green900, label = "BorderColor")

    Column(
        modifier = Modifier.background(Color.Transparent)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 7.dp),
            fontFamily = PlusJakartaSans
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = isEnabled,
            singleLine = singleLine,
            isError = isError,
            supportingText = supportingText,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = PlusJakartaSans
            ),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_visibility_off_24 else R.drawable.ic_visibility_24
                            ),
                            contentDescription = "Toggle Password",
                            tint = Grey500
                        )
                    }
                } else if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_24),
                            contentDescription = "Clear Text",
                            tint = Grey500
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            placeholder = { Text(text = placeholder, fontSize = 16.sp, color = Color.Gray, fontFamily = PlusJakartaSans) },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Grey500,
                focusedContainerColor = if (isError) Color(0xFFFFEBEE) else Color.White,
                unfocusedContainerColor = if (isError) Color(0xFFFFEBEE) else Color.White,
                errorContainerColor = Color(0xFFFFEBEE),
                disabledIndicatorColor = borderColor,
                unfocusedIndicatorColor = if (isError) Color.Red else borderColor,
                focusedIndicatorColor = if (isError) Color.Red else borderColor,
                errorIndicatorColor = Color.Red,
            )
        )
    }
}
