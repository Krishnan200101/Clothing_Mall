package com.example.clothingmall.viewmodel

import com.example.clothingmall.data.Review

sealed class ReviewsState {
    object Loading : ReviewsState()
    data class Success(val reviews: List<Review>) : ReviewsState()
    data class Error(val message: String) : ReviewsState()
} 