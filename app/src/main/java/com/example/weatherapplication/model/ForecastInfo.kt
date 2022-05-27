package com.example.weatherapplication.model

data class ForecastInfo(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)

data class Current(
    val precip_in: Double,
    val precip_mm: Double,
    val pressure_in: Double,
    val pressure_mb: Double,
    val temp_c: Double,
    val temp_f: Double,
    val uv: Double,
    val air_quality : AirQuality
)

data class AirQuality(
    val co: Double,
    val no2 : Double,
    val o3 : Double,
    val so2 : Double,
    val pm2_5: Double,
    val pm10 : Double
)

data class Forecast(
    val forecastday : List<Forecastday>
)

data class Location(
    val country: String,
    val localtime: String,
    val localtime_epoch: Int,
    val name: String,
    val region: String,
    val tz_id: String
)

data class Forecastday(
    val date: String,
    val date_epoch: Int,
    val day: Day,
)

data class Day(
    val avgvis_km: Double,
    val condition: ConditionX,
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val totalprecip_mm: Double,
    val uv: Double
)

data class ConditionX(
    val code: Int,
    val icon: String,
    val text: String
)