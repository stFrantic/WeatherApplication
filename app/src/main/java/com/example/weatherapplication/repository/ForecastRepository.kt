package com.example.weatherapplication.repository

import com.example.weatherapplication.network.ForecastApiService

import javax.inject.Inject

class ForecastRepository @Inject constructor(private val forecastService: ForecastApiService) {
    suspend fun getForecast(lat: String, lon: String) =
        forecastService.getForecast("$lat,$lon", "5", "yes", "no")
}