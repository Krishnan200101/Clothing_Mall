package com.example.clothingmall.data

import android.content.Context
import com.example.clothingmall.api.RetrofitInstance
import com.example.clothingmall.utils.NetworkConnectivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class ReviewRepository(private val context: Context) {
    private val gson = Gson()
    private val localFile = File(context.filesDir, "reviews.json")
    private val api = RetrofitInstance.reviewApi
    private val networkConnectivity = NetworkConnectivity(context)

    private suspend fun loadDefaultReviews(): List<Review> {
        return withContext(Dispatchers.IO) {
            try {
                context.assets.open("default_reviews.json").use { inputStream ->
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val type = object : TypeToken<List<Review>>() {}.type
                    gson.fromJson(jsonString, type) ?: emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private suspend fun saveReviewsLocally(reviews: List<Review>) {
        withContext(Dispatchers.IO) {
            try {
                // Save to local storage file
                localFile.writeText(gson.toJson(reviews))
                
                // Also update default_reviews.json content
                context.openFileOutput("default_reviews.json", Context.MODE_PRIVATE).use { output ->
                    output.write(gson.toJson(reviews).toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getLocalReviews(): List<Review> {
        return withContext(Dispatchers.IO) {
            try {
                if (localFile.exists()) {
                    val json = localFile.readText()
                    val type = object : TypeToken<List<Review>>() {}.type
                    gson.fromJson(json, type) ?: loadDefaultReviews()
                } else {
                    loadDefaultReviews()
                }
            } catch (e: IOException) {
                loadDefaultReviews()
            }
        }
    }

    suspend fun getReviews(): List<Review> {
        return try {
            if (networkConnectivity.isConnected) {
                try {
                    val response = api.getReviews()
                    if (response.isSuccessful) {
                        val reviews = response.body() ?: emptyList()
                        saveReviewsLocally(reviews)
                        reviews
                    } else {
                        getLocalReviews()
                    }
                } catch (e: Exception) {
                    getLocalReviews()
                }
            } else {
                getLocalReviews()
            }
        } catch (e: Exception) {
            getLocalReviews()
        }
    }

    suspend fun addReview(review: Review) {
        withContext(Dispatchers.IO) {
            try {
                // First save locally
                val currentReviews = getLocalReviews().toMutableList()
                currentReviews.add(review)
                saveReviewsLocally(currentReviews)

                // Then try to sync with server if online
                if (networkConnectivity.isConnected) {
                    try {
                        val response = api.postReview(review)
                        if (!response.isSuccessful) {
                            throw Exception("Failed to post review")
                        }
                    } catch (e: Exception) {
                        // If server sync fails, we already have the review saved locally
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}