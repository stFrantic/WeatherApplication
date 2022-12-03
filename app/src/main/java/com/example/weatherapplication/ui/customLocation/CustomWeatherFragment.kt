package com.example.weatherapplication.ui.customLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapplication.databinding.FragmentCustomWeatherBinding

class CustomWeatherFragment: Fragment() {

    private lateinit var binding : FragmentCustomWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomWeatherBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
        private var fragmentCustomWeather : CustomWeatherFragment? = null
        fun newInstance(): CustomWeatherFragment{
            if(fragmentCustomWeather == null)
                fragmentCustomWeather = CustomWeatherFragment()
            return fragmentCustomWeather as CustomWeatherFragment
        }
    }
}