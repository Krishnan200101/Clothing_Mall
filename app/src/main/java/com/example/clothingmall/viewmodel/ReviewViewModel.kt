package com.example.clothingmall.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clothingmall.data.Review
import com.example.clothingmall.data.ReviewRepository
import com.example.clothingmall.utils.NetworkConnectivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(context: Context) : ViewModel() {
    private val repository = ReviewRepository(context)
    private val networkConnectivity = NetworkConnectivity(context)
    
    private val _reviewsState = MutableStateFlow<ReviewsState>(ReviewsState.Loading)
    val reviewsState: StateFlow<ReviewsState> = _reviewsState
    
    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline

    init {
        loadReviews()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            _isOffline.value = !networkConnectivity.isConnected
        }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            _reviewsState.value = ReviewsState.Loading
            try {
                val reviews = repository.getReviews()
                _reviewsState.value = ReviewsState.Success(reviews)
            } catch (e: Exception) {
                _reviewsState.value = ReviewsState.Error(e.message ?: "Error loading reviews")
            }
        }
    }

    fun addReview(userName: String, rating: Int, comment: String) {
        viewModelScope.launch {
            try {
                _reviewsState.value = ReviewsState.Loading
                val review = Review(
                    userName = userName,
                    rating = rating,
                    comment = comment
                )
                repository.addReview(review)
                loadReviews()
            } catch (e: Exception) {
                _reviewsState.value = ReviewsState.Error(e.message ?: "Error adding review")
            }
        }
    }
}

class ReviewViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 