package com.example.clothingmall.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://petlandshop.store/"

    val api: ReviewApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to Kotlin objects
            .build()
            .create(ReviewApiService::class.java)
    }
}
