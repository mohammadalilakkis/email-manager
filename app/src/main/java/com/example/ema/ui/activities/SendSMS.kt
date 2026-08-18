package com.example.ema.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ema.ui.theme.EmaTheme
import com.example.ema.ui.theme.md_theme_dark_surfaceTint
import com.github.skydoves.colorpicker.compose.*

class SendSMS : ComponentActivity() {

    companion object {
        private const val REQUEST_SEND_SMS_PERMISSION = 123
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                        arrayOf(Manifest.permission.SEND_SMS),
                        REQUEST_SEND_SMS_PERMISSION
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background

                ) {

                    Scaffold(

                        topBar = {

                            TopAppBar(
                                modifier = Modifier.background(md_theme_dark_surfaceTint),
                                title = {
                                    Text(
                                        text = "Send SMS",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }) {

                        smsUI(context = LocalContext.current)
                    }

                }

            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun smsUI(context: Context) {

        val phoneNumber = remember {
            mutableStateOf("")
        }
        val message = remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "SMS Manager",
                color = md_theme_dark_surfaceTint   ,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextField(
                value = phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
                placeholder = { Text(text = "Enter your phone number") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = message.value,
                onValueChange = { message.value = it },
                placeholder = { Text(text = "Enter your message") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                try {
                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNumber.value, null, message.value, null, null)

                    Toast.makeText(
                        context,
                        "Message Sent",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {

                    Toast.makeText(
                        context,
                        "Error : " + e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }) {
                Text(

                    text = "Send SMS",
                    modifier = Modifier.padding(10.dp),
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }
    }
}