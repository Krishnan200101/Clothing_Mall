package com.example.clothingmall.viewmodel

sealed class LocationState {
    object Initial : LocationState()
    object Loading : LocationState()
    data class Success(
        val temperature: Float,
        val clothingSuggestion: String
    ) : LocationState()
    data class Error(val message: String) : LocationState()
} 