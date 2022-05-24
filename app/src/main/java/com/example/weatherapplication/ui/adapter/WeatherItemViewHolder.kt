package com.example.weatherapplication.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.databinding.ItemWeatherBinding
import com.example.weatherapplication.loadUrl
import com.example.weatherapplication.model.ForecastCard

class WeatherItemViewHolder(private val binding: ItemWeatherBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(item:ForecastCard){
        binding.apply {
            date.text = item.day
            temp.text = item.temperature
            icon.loadUrl(item.iconUrl)
        }
    }

}