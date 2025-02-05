package com.example.clothingmall.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingmall.api.RetrofitInstance
import com.example.clothingmall.data.ExternalData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ExternalDataState {
    object Loading : ExternalDataState()
    data class Success(val data: ExternalData) : ExternalDataState()
    data class Error(val message: String) : ExternalDataState()
}

class ExternalDataViewModel : ViewModel() {
    private val _state = MutableStateFlow<ExternalDataState>(ExternalDataState.Loading)
    val state: StateFlow<ExternalDataState> = _state

    fun loadExternalData() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.externalApi.getExternalData()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _state.value = ExternalDataState.Success(it)
                    } ?: run {
                        _state.value = ExternalDataState.Error("Empty response")
                    }
                } else {
                    _state.value = ExternalDataState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = ExternalDataState.Error(e.message ?: "Unknown error")
            }
        }
    }
} 