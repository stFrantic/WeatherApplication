package com.example.weatherapplication.repository

import com.example.weatherapplication.network.ForecastApiService
import com.example.weatherapplication.network.WeatherApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: WeatherApiService,
    private val forecastService: ForecastApiService
) {

    suspend fun getCurrentWeather(lat: String, lon: String) =
        weatherService.getWeather(lat, lon, "metric")

    suspend fun getForecast(lat: String, lon: String) =
        forecastService.getForecast("$lat,$lon", "5", "yes", "no")
}