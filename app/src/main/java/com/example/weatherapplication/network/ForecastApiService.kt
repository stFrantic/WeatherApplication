package com.example.weatherapplication.network

import com.example.weatherapplication.model.ForecastInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val FORECAST_API_KEY = "621e3e3c42924c4daca143409222405"

interface ForecastApiService {

    @GET("forecast.json?key=${FORECAST_API_KEY}")
    suspend fun getForecast(
        @Query("q") lat_lon: String,
        @Query("days") days: String,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String
    ): Response<ForecastInfo>

}
