package com.example.weatherapplication.network

import com.example.weatherapplication.model.WeatherInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_API_KEY = "281a8ba4ace94b044ab25a1325b4ce66"

interface WeatherApiService {

    @GET("weather?appid=${WEATHER_API_KEY}")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): Response<WeatherInfo>
}