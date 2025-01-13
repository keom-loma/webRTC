package com.example.webrtc

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * @Author: Jupyter.
 * @Date: 1/13/25.
 * @Email: koemheang200@gmail.com.
 */

@Composable
fun NetworkMonitor(onNetworkChanged:(Boolean)->Unit) {
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isConnected = remember { mutableStateOf(true) }



    DisposableEffect(Unit) {
        val networkCallBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                isConnected.value = true
                onNetworkChanged(true)
            }

            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                isConnected.value = false
                onNetworkChanged(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallBack)
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallBack)
        }
    }
}