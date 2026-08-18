package com.example.ema

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ema.data.cookies.CookieDatabase
import com.example.ema.data.cookies.CookieEntity
import com.example.ema.ui.activities.DashboardActivity
import com.example.ema.ui.activities.NoInternetActivity
import com.example.ema.ui.activities.SignupActivity
import com.example.ema.ui.theme.EmaTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Opening(intent)
                }
            }
        }
    }
}

@Composable
@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
fun Opening(intent: Intent) {
    val context = LocalContext.current
    if (!isConnectedToInternet(context)) {
        val intent = Intent()
        intent.setClass(context, NoInternetActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        return
    }

    val db = CookieDatabase(context)
    GlobalScope.launch {
        val pageIntent = Intent()
        var token: CookieEntity? = null
        if (intent.getBooleanExtra("logout", false))
            db.cookieDao().deleteAllCookies()
        token = db.cookieDao().getCookieByName("token")
        if (token == null) {
            pageIntent.setClass(context, SignupActivity::class.java)
        } else pageIntent.setClass(context, DashboardActivity::class.java)
        pageIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(pageIntent)
    }
}

@Preview(showBackground = true)
@Composable
fun EmaPreview() {
    EmaTheme {
        Opening(Intent())
    }
}

private fun isConnectedToInternet(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false

    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}