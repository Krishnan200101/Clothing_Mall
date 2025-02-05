package com.example.clothingmall

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clothingmall.utils.NetworkConnectivity
import com.example.clothingmall.viewmodel.AppViewModel
import com.example.clothingmall.viewmodel.AppViewModelFactory

@Composable
fun WeatherClothingScreen() {
    val context = LocalContext.current
    val viewModel: AppViewModel = viewModel(
        factory = AppViewModelFactory(context)
    )
    val networkConnectivity = remember { NetworkConnectivity(context) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!networkConnectivity.isConnected) {
            Text(
                "No internet connection",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Text(
            text = viewModel.batteryStatus,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = viewModel.temperature,
            onValueChange = { viewModel.updateTemperature(it) },
            label = { Text("Enter Temperature (°C)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.clothingSuggestion.isNotEmpty()) {
            Text(viewModel.clothingSuggestion)
        }
    }
} 