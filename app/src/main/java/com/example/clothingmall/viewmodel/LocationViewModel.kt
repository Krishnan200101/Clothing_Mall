package com.example.clothingmall.viewmodel

import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clothingmall.api.ApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class LocationViewModel(private val context: Context) : ViewModel() {
    private val fusedLocationClient: FusedLocationProviderClient = 
        LocationServices.getFusedLocationProviderClient(context)
    private val weatherApi = ApiClient.createWeatherApi()
    private val WEATHER_API_KEY = "4b13103c0130df0d1863462bab4714e3\n" // Remove the newline character

    private val _locationState = MutableStateFlow<LocationState>(LocationState.Initial)
    val locationState: StateFlow<LocationState> = _locationState

    var locationQuery by mutableStateOf("")
        private set

    fun updateLocationQuery(query: String) {
        locationQuery = query
    }

    fun searchLocation() {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(locationQuery, 1)
                
                if (addresses.isNullOrEmpty()) {
                    _locationState.value = LocationState.Error("Location not found")
                    return@launch
                }

                val location = addresses[0]
                fetchWeather(location.latitude, location.longitude)
            } catch (e: Exception) {
                _locationState.value = LocationState.Error(e.message ?: "Error fetching location")
            }
        }
    }

    private suspend fun fetchWeather(lat: Double, lon: Double) {
        try {
            val response = weatherApi.getWeather(
                lat = lat,
                lon = lon,
                apiKey = WEATHER_API_KEY.trim() // Trim any whitespace or newlines
            )

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    _locationState.value = LocationState.Success(
                        temperature = weatherResponse.main.temp,
                        clothingSuggestion = getClothingSuggestion(weatherResponse.main.temp)
                    )
                } ?: run {
                    _locationState.value = LocationState.Error("Weather data not available")
                }
            } else {
                _locationState.value = LocationState.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            _locationState.value = LocationState.Error(e.message ?: "Error fetching weather")
        }
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        viewModelScope.launch {
                            try {
                                val response = weatherApi.getWeather(
                                    location.latitude,
                                    location.longitude,
                                    WEATHER_API_KEY
                                )

                                if (response.isSuccessful) {
                                    val weatherData = response.body()
                                    weatherData?.let {
                                        _locationState.value = LocationState.Success(
                                            temperature = it.main.temp,
                                            clothingSuggestion = getClothingSuggestion(it.main.temp)
                                        )
                                    } ?: run {
                                        _locationState.value = LocationState.Error("Weather data not available")
                                    }
                                } else {
                                    _locationState.value = LocationState.Error("Error fetching weather data")
                                }
                            } catch (e: Exception) {
                                _locationState.value = LocationState.Error(e.message ?: "Error fetching weather data")
                            }
                        }
                    } else {
                        _locationState.value = LocationState.Error("Location not available")
                    }
                }
            } catch (e: Exception) {
                _locationState.value = LocationState.Error(e.message ?: "Error getting location")
            }
        }
    }

    private fun getClothingSuggestion(temperature: Float): String {
        return when {
            temperature >= 25 -> "It's warm! Wear light clothes like t-shirts, shorts and sandals"
            temperature < 15 -> "It's cold! Wear warm clothes like jackets, sweaters, and boots"
            else -> "The temperature is normal. Wear casual clothes like jeans, t-shirts, and sneakers"
        }
    }
}

class LocationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 