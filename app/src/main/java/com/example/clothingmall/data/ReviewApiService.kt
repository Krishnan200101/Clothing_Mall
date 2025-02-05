package com.example.clothingmall.data


import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call

interface ReviewApiService {
    @GET("api/reviews")
    fun getReviews(): Call<List<Review>>

    @POST("api/reviews")
    fun postReview(@Body review: Review): Call<Void>
}
