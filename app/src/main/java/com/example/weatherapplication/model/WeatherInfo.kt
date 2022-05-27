package com.example.weatherapplication.model

data class WeatherInfo(
    val base: String,
    val cod: Int,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val weather: List<Weather>,
)

data class Main(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)