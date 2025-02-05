package com.example.clothingmall.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}

data class WeatherResponse(
    val main: Main,
    val name: String
)

data class Main(
    val temp: Float,
    val feels_like: Float,
    val humidity: Int
) 