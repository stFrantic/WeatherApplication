package com.example.weatherapplication.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.databinding.ItemWeatherBinding
import com.example.weatherapplication.model.ForecastCard

class WeatherItemAdapter(private val context: Context) :
    RecyclerView.Adapter<WeatherItemViewHolder>() {
    private var listForecastCards = arrayListOf<ForecastCard>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherItemViewHolder(ItemWeatherBinding.inflate(LayoutInflater.from(context)))

    override fun onBindViewHolder(holder: WeatherItemViewHolder, pos: Int) {
        holder.bind(listForecastCards[pos])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<ForecastCard>) {
        listForecastCards = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = listForecastCards.size
}