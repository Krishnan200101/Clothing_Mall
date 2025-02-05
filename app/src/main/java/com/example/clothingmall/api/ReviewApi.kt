package com.example.clothingmall.api

import com.example.clothingmall.data.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReviewApi {
    @GET("api/reviews")
    suspend fun getReviews(): Response<List<Review>>

    @POST("api/reviews")
    suspend fun postReview(@Body review: Review): Response<Review>
} 