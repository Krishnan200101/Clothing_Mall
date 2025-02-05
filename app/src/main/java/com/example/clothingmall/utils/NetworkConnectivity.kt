package com.example.clothingmall.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class NetworkConnectivity(context: Context) {
    private val connectivityManager = 
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.value = true
        }

        override fun onLost(network: Network) {
            _isConnected.value = false
        }
    }

    private val _isConnected = mutableStateOf(false)
    val isConnected: Boolean get() = _isConnected.value

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun cleanup() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

@Composable
fun rememberNetworkConnectivity(): NetworkConnectivity {
    val context = LocalContext.current
    val networkConnectivity = remember { NetworkConnectivity(context) }

    DisposableEffect(networkConnectivity) {
        onDispose {
            networkConnectivity.cleanup()
        }
    }

    return networkConnectivity
} 