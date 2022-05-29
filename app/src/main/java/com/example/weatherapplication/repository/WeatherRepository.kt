package com.example.weatherapplication.repository

import com.example.weatherapplication.network.WeatherApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: WeatherApiService,
) {

    suspend fun getCurrentWeather(lat: String, lon: String) =
        weatherService.getWeather(lat, lon, "metric")
}