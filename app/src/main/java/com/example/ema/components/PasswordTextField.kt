package com.example.ema.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GeneratePasswordTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
) {
    GenerateOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        errorMessage = errorMessage,
        modifier = modifier,
        shape = shape,
        colors = colors,
        visualTransformation = PasswordVisualTransformation(),
        keyboardType = KeyboardType.Password
    )
}