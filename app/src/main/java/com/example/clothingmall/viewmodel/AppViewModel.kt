package com.example.clothingmall.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clothingmall.sensors.DeviceSensors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(context: Context) : ViewModel() {
    private val deviceSensors = DeviceSensors(context)
    
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    var temperature by mutableStateOf("")
        private set
    
    var clothingSuggestion by mutableStateOf("")
        private set

    var batteryStatus by mutableStateOf("")
        private set

    init {
        updateBatteryStatus()
    }

    fun updateTemperature(newTemp: String) {
        temperature = newTemp
        newTemp.toFloatOrNull()?.let { temp ->
            clothingSuggestion = deviceSensors.getClothingSuggestionForTemperature(temp)
        }
    }

    private fun updateBatteryStatus() {
        viewModelScope.launch {
            batteryStatus = deviceSensors.checkBatteryStatus()
        }
    }

    fun updateUiState(update: (AppUiState) -> AppUiState) {
        _uiState.value = update(_uiState.value)
    }

    override fun onCleared() {
        super.onCleared()
        deviceSensors.cleanup()
    }
}

data class AppUiState(
    val isLoading: Boolean = false,
    val isConnected: Boolean = true,
    val error: String? = null
)

class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 