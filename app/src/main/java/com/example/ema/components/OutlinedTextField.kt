package com.example.ema.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GenerateOutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1
) {
    val isError by remember(errorMessage, value) {
        derivedStateOf {
            errorMessage.isNotBlank()
        }
    }

    OutlinedTextField(
        maxLines = maxLines,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        shape = shape,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(text = errorMessage)
            }
        },
        colors = colors,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}