package com.example.weatherapplication.network

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val API_BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val FORECAST_API_BASE_URL = "http://api.weatherapi.com/v1/"

@Module
@InstallIn(ViewModelComponent::class)
class NetworkManager {

    @Provides
    fun provideUnauthorizedCachedRequestsWeatherApi(): WeatherApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    fun provideUnauthorizedCachedRequestsForecastApi(): ForecastApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(FORECAST_API_BASE_URL)
            .client(createHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(ForecastApiService::class.java)
    }

    private fun createHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor { message ->
                    Log.d(
                        "OK_HTTP", message
                    )
                }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
        return builder.build()
    }
}