package com.example.ema.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ema.MainActivity
import com.example.ema.ui.theme.EmaTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    DashboardPage()
                }
            }
        }
    }
}

@Composable
fun DashboardPage() {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            ) {
                GenerateRoundedOutlineButton(
                    text = "Mail compose", icon = Icons.Rounded.Send,
                    goTo = MailComposeActivity::class.java, context
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            ) {
                GenerateRoundedOutlineButton(
                    text = "Outbox", icon = Icons.Rounded.Email,
                    goTo = OutboxActivity::class.java, context
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            ) {
                GenerateRoundedOutlineButton(
                    text = "Contacts", icon = Icons.Rounded.Person,
                    goTo = ContactListActivity::class.java, context
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            ) {
                GenerateRoundedOutlineButton(
                    text = "Campaigns", icon = Icons.Rounded.Favorite,
                    goTo = CampaignActivity::class.java, context
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            GenerateRoundedOutlineButton(
                text = "Logout", icon = Icons.Rounded.ExitToApp,
                goTo = MainActivity::class.java, context,
                isLogout = true
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewDashboard() {
    EmaTheme {
        DashboardPage()
    }
}

@Composable
private fun GenerateRoundedOutlineButton(
    text: String,
    icon: ImageVector,
    goTo: Class<*>,
    context: Context,
    isLogout: Boolean = false
) {
    Button(
        onClick = {
            val intent = Intent(context, goTo)
            if (isLogout) intent.putExtra("logout", true)
            context.startActivity(intent)
        },
        Modifier
            .height(150.dp)
            .fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp)
            )
        }
    }
}
