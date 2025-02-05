package com.example.clothingmall

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clothingmall.viewmodel.LocationViewModel
import com.example.clothingmall.viewmodel.LocationViewModelFactory
import com.example.clothingmall.viewmodel.LocationState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices

@Composable
fun LocationSuggestionsScreen() {
    val context = LocalContext.current
    val viewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(context)
    )
    val locationState by viewModel.locationState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Location-based Suggestions",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = viewModel.locationQuery,
            onValueChange = { viewModel.updateLocationQuery(it) },
            label = { Text("Enter Location") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.searchLocation() },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Search Location")
            }

            Button(
                onClick = { viewModel.getCurrentLocation() },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Use Current Location")
            }
        }

        when (val state = locationState) {
            LocationState.Initial -> {
                Text(
                    text = "Enter a location or use current location",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            LocationState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            is LocationState.Success -> {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Temperature: ${state.temperature}°C",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = state.clothingSuggestion,
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            is LocationState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
} 