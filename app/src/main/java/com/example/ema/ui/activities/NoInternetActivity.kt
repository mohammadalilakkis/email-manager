package com.example.ema.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ema.MainActivity
import com.example.ema.R
import com.example.ema.ui.theme.EmaTheme

class NoInternetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
        setContent {
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InternetIssuePage()
                }
            }
        }
    }
}

@Composable
fun InternetIssuePage() {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.width(100.dp).height(100.dp),
            painter = painterResource(id = R.drawable.wifi),
            contentDescription = "no internet",
        )
        Text(text = "You're not connected to internet!")
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            shape = RoundedCornerShape(10.dp),
            onClick = {
                val intent = Intent()
                intent.setClass(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        ) {
            Text(text = "Try again")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
    EmaTheme {
        InternetIssuePage()
    }
}