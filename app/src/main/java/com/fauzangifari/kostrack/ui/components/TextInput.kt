package com.fauzangifari.kostrack.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.fauzangifari.kostrack.ui.theme.Green800
import com.fauzangifari.kostrack.ui.theme.Green900
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import com.fauzangifari.kostrack.R
import com.fauzangifari.kostrack.ui.theme.ErrorBase
import com.fauzangifari.kostrack.ui.theme.Grey500

@Composable
fun TextInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    errorText: String? = null,
    trailingIconClick: (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    val isError = !errorText.isNullOrEmpty()

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> ErrorBase
            isFocused -> Green800
            else -> Green900.copy(alpha = 0.5f)
        }, 
        label = "BorderColor"
    )

    val labelColor by animateColorAsState(
        targetValue = if (isError) ErrorBase else Color.Black,
        label = "LabelColor"
    )

    Column(
        modifier = Modifier.fillMaxWidth().background(Color.Transparent)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = labelColor,
            fontWeight = if (isFocused || isError) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp),
            fontFamily = PlusJakartaSans
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = isEnabled,
            singleLine = singleLine,
            isError = isError,
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
                            tint = if (isError) ErrorBase else Grey500
                        )
                    }
                } else if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_24),
                            contentDescription = "Clear Text",
                            tint = if (isError) ErrorBase else Grey500
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            shape = RoundedCornerShape(12.dp),
            placeholder = { 
                Text(
                    text = placeholder, 
                    fontSize = 16.sp, 
                    color = Color.Gray.copy(alpha = 0.6f), 
                    fontFamily = PlusJakartaSans 
                ) 
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                errorBorderColor = ErrorBase,
                cursorColor = Green800,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color(0xFFFFFBFA),
                disabledContainerColor = Color(0xFFF5F5F5)
            )
        )

        AnimatedVisibility(
            visible = isError,
            enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
            exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
        ) {
            Row(
                modifier = Modifier.padding(top = 6.dp, start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = null,
                    tint = ErrorBase,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = errorText ?: "",
                    color = ErrorBase,
                    fontSize = 12.sp,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}