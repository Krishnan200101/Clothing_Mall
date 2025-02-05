package com.example.clothingmall.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clothingmall.data.Review
import com.example.clothingmall.viewmodel.ReviewViewModel
import com.example.clothingmall.viewmodel.ReviewViewModelFactory
import com.example.clothingmall.viewmodel.ReviewsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ReviewScreen(
    onReviewSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ReviewViewModel = viewModel(
        factory = ReviewViewModelFactory(context)
    )
    
    var rating by remember { mutableStateOf("5") }
    var comment by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val reviewsState by viewModel.reviewsState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Add a Review",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Your Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = rating,
            onValueChange = { input ->
                if (input.isEmpty() || input.length <= 1) {
                    val newRating = input.toIntOrNull()
                    if (input.isEmpty() || (newRating != null && newRating in 1..5)) {
                        rating = input
                        showError = false
                    } else {
                        showError = true
                    }
                }
            },
            label = { Text("Rating (1-5)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = showError,
            supportingText = if (showError) {
                { Text("Please enter a number between 1 and 5") }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                if (rating.isNotEmpty() && comment.isNotEmpty() && userName.isNotEmpty()) {
                    val ratingNum = rating.toIntOrNull() ?: 5
                    if (ratingNum in 1..5) {
                        viewModel.addReview(
                            userName = userName,
                            rating = ratingNum,
                            comment = comment
                        )
                        // Clear the form
                        rating = ""
                        comment = ""
                        userName = ""
                        showError = false
                    } else {
                        showError = true
                    }
                }
            },
            enabled = !showError && rating.isNotEmpty() && comment.isNotEmpty() && userName.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Submit Review")
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "All Reviews",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (reviewsState) {
            is ReviewsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is ReviewsState.Success -> {
                LazyColumn {
                    items((reviewsState as ReviewsState.Success).reviews) { review ->
                        ReviewItem(review)
                    }
                }
            }
            is ReviewsState.Error -> {
                Text(
                    text = (reviewsState as ReviewsState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "By: ${review.userName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Rating: ${review.rating}/5",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
} 