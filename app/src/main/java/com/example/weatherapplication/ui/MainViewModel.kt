package com.example.weatherapplication.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapplication.model.ForecastInfo
import com.example.weatherapplication.model.WeatherInfo
import com.example.weatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
private val repository: WeatherRepository
) : ViewModel() {

    val info = MutableLiveData<WeatherInfo>()
    val forecast = MutableLiveData<ForecastInfo>()

   suspend fun getInfo(lat:String, lon :String){
        repository.getCurrentWeather(lat,lon).apply {
            if(isSuccessful){
                info.postValue(this.body())
            }else{
                Log.d("Weather Api error",this.message())
            }

        }
       repository.getForecast(lat,lon).apply {
           if(isSuccessful){
               forecast.postValue(this.body())
           }else{
               Log.d("Weather Api error",this.message())
           }
       }
    }

}