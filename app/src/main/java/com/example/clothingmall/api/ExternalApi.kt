package com.example.clothingmall.api

import com.example.clothingmall.data.ExternalData
import retrofit2.Response
import retrofit2.http.GET

interface ExternalApi {
    @GET("typicode/demo/master/db.json")
    suspend fun getExternalData(): Response<ExternalData>
} 