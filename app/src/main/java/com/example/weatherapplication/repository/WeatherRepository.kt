package com.example.weatherapplication.repository

import com.example.weatherapplication.network.WeatherApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val service: WeatherApiService) {

    suspend fun getCurrentWeather(lat: String, lon: String) = service.getWeather(lat, lon, "metric")
}