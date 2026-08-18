package com.example.ema.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ema.api.Repository.UserRepository
import com.example.ema.components.GenerateOutlinedTextField
import com.example.ema.components.GeneratePasswordTextField
import com.example.ema.model.ClientDto
import com.example.ema.ui.theme.EmaTheme
import com.example.ema.ui.theme.Typography
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LoginPage()
                }
            }
        }
    }
}

@Composable
fun LoginPage() {
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    val validationErrors = remember { mutableStateOf(emptyMap<String, String>()) }

    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(18.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenerateOutlinedTextField(
            value = username, label = "Username",
            onValueChange = { newValue ->
                username = newValue
                validationErrors.value =
                    validationErrors.value + ("username" to validateUsername(newValue))
            },
            errorMessage = validationErrors.value["username"] ?: "",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        GeneratePasswordTextField(
            value = password, label = "Password",
            onValueChange = { newValue ->
                password = newValue
                validationErrors.value =
                    validationErrors.value + ("password" to validatePassword(newValue))
            },
            errorMessage = validationErrors.value["password"] ?: "",
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Button(
                onClick = {
                    loginUser(
                        validationErrors = validationErrors.value,
                        context = context, username = username.text,
                        password = password.text
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Login", style = Typography.bodyLarge)
            }
        }
        Row {
            TextButton(
                onClick = {
                    val intent = Intent(context, SignupActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "Don't have account yet",
                    style = Typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginPage() {
    EmaTheme { LoginPage() }
}

private fun loginUser(
    validationErrors: Map<String, String>,
    context: Context,
    username: String, password: String
) {
    val result = validationErrors.filter { (key, value) -> value.isNotBlank() }
    if (result.isNotEmpty() || validationErrors.isEmpty()) {
        Toast.makeText(context, "Fill all the fields correctly!", Toast.LENGTH_SHORT).show()
        return
    }
    GlobalScope.launch{
        val client = ClientDto(username = username, password = password, email = "", name = "")
        val response = UserRepository(context).loginUser(client)
        if (response != null) {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}

private fun validateUsername(value: TextFieldValue): String {
    return when {
        value.text.isEmpty() -> "Enter username"
        value.text.length < 6 -> "Username must have at least 6 characters"
        else -> ""
    }
}

private fun validatePassword(value: TextFieldValue): String {
    return if (value.text.isEmpty()) "Enter password" else ""
}