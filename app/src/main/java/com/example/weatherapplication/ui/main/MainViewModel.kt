package com.example.weatherapplication.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.model.ForecastInfo
import com.example.weatherapplication.model.SavedLocation
import com.example.weatherapplication.model.WeatherInfo
import com.example.weatherapplication.repository.ForecastRepository
import com.example.weatherapplication.repository.SavedLocationRepository
import com.example.weatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: SavedLocationRepository,
    private val repository: WeatherRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    val locations = MutableLiveData<ArrayList<SavedLocation>>(arrayListOf())
    val info = MutableLiveData<WeatherInfo>()
    val forecast = MutableLiveData<ForecastInfo>()

    fun getInfo(key: Long) {
        viewModelScope.launch {
            getWeatherInfo(
                locationRepository.getLocationByKey(key).lat.toString(),
                locationRepository.getLocationByKey(key).lon.toString()
            )
        }
    }

    fun getInfo(lat: String, lon: String) {
        getWeatherInfo(lat, lon)
    }

    private fun getWeatherInfo(lat: String, lon: String) {
        viewModelScope.launch {
            val apiF = forecastRepository.getForecast(lat, lon)
            val apiI = repository.getCurrentWeather(lat, lon)

            apiF.apply {
                if (isSuccessful) {
                    forecast.postValue(this.body())
                } else {
                    Log.d("Forecast Api error", this.message())
                }
            }
            apiI.apply {
                if (isSuccessful) {
                    info.postValue(this.body())
                } else {
                    Log.d("Weather Api error", this.message())
                }
            }
        }
    }

    fun getLocations() {
        viewModelScope.launch {
            locations.postValue(locationRepository.getLocations() as ArrayList<SavedLocation>)
        }
    }

    fun saveLocation(location: SavedLocation): Boolean {
        for (_location in locations.value!!) {
            if (_location.name == location.name)
                return false
        }
        viewModelScope.launch {
            locationRepository.addLocation(location)
            getLocations()
        }
        return true
    }
}