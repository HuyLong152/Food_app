package com.example.food_app.data.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

class BroadcastReceiverInternet(private val context: Context) : BroadcastReceiver() {
    private var lostConnectionNotified = false

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "android.net.conn.CONNECTIVITY_CHANGE" -> {
                val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo == null || !networkInfo.isConnected) {
                    if (!lostConnectionNotified) {
                        Toast.makeText(context, "Lost internet connection", Toast.LENGTH_SHORT).show()
                        lostConnectionNotified = true
                    }
                } else {
                    if (lostConnectionNotified) {
                        Toast.makeText(context, "The network has been restored", Toast.LENGTH_SHORT).show()
                        lostConnectionNotified = false
                    }
                }
            }
            "android.intent.action.ACTION_POWER_CONNECTED" -> {
                Toast.makeText(context, "Power connected", Toast.LENGTH_SHORT).show()
            }
            "android.intent.action.ACTION_POWER_DISCONNECTED" -> {
                Toast.makeText(context, "Power disconnected", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                if (isAirplaneModeOn(context)) {
                    Toast.makeText(context, "Airplane mode turned on", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Airplane mode turned off", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isAirplaneModeOn(context: Context?): Boolean {
        return Settings.Global.getInt(context?.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }
}

