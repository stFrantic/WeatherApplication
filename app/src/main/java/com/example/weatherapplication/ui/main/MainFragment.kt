package com.example.weatherapplication.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.databinding.FragmentMainBinding
import com.example.weatherapplication.formatedDouble
import com.example.weatherapplication.getDay
import com.example.weatherapplication.isOnline
import com.example.weatherapplication.model.ForecastCard
import com.example.weatherapplication.model.Main
import com.example.weatherapplication.setAnimatedIcon
import com.example.weatherapplication.ui.adapter.WeatherItemAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val celsius = "\u00B0C"
    private val dim = " μg/m\u00B3"

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isOnline(requireContext())) {

            viewModel.info.observe(viewLifecycleOwner) {
                binding.apply {
                    progressBar.visibility = View.GONE
                    temperature.text = it.main.temp.roundToInt().toString() + celsius
                    icon.setAnimatedIcon(it.weather[0].main, it.weather[0].description)
                    feelsLike.text =
                        "Feels like: ${it.main.feels_like.roundToInt()}" + celsius
                    pressure.text = "Pressure: ${it.main.pressure}hPa"
                    humidity.text = "Humidity: ${it.main.humidity}%"
                }
            }
            viewModel.forecast.observe(viewLifecycleOwner) {
                val current = it.forecast.forecastday[0].day
                val list = arrayListOf<ForecastCard>()
                binding.apply {
                    highLowTemp.text =
                        "High: ${current.maxtemp_c.roundToInt()}" + celsius + " Low: ${current.mintemp_c.roundToInt()}" + celsius
                    precipitation.text = "Precipitation: ${current.totalprecip_mm} mm"
                    uvIndex.text = "UV index: ${current.uv}"
                    visibility.text = "Visibility: ${current.avgvis_km} km"
                    co.text =
                        "CO: " + formatedDouble(it.current.air_quality.co).replace(",", ".") + dim
                    no2.text = "NO\u2082: " + formatedDouble(it.current.air_quality.no2).replace(
                        ",",
                        "."
                    ) + dim
                    so2.text = "SO\u2082: " + formatedDouble(it.current.air_quality.so2).replace(
                        ",",
                        "."
                    ) + dim
                    o3.text =
                        "O\u2083: " + formatedDouble(it.current.air_quality.o3).replace(
                            ",",
                            "."
                        ) + dim
                    pm25.text =
                        "PM2.5: " + formatedDouble(it.current.air_quality.pm2_5).replace(
                            ",",
                            "."
                        ) + dim
                    pm10.text =
                        "PM10: " + formatedDouble(it.current.air_quality.pm10).replace(
                            ",",
                            "."
                        ) + dim
                    airQuality.setAirQuality(it.current.air_quality.us_epa_index)
                }
                for (i in 1 until it.forecast.forecastday.size) {
                    list.add(
                        ForecastCard(
                            getDay(it.forecast.forecastday[i].date_epoch, it.location.tz_id),
                            it.forecast.forecastday[i].day.maxtemp_c.roundToInt()
                                .toString() + celsius,
                            "https://${it.forecast.forecastday[i].day.condition.icon}"
                        )
                    )
                }
                setCards(list)
            }
        }
        else{
            binding.progressBar.visibility = View.GONE
            Toast.makeText(
                requireContext(),
                "No internet connection",
                Toast.LENGTH_LONG
            ).show()
            binding.noInternetImage.visibility = View.VISIBLE
            Log.d("Connection problems","No internet connection.")
        }
    }

    override fun onResume() {
        super.onResume()
        if (isOnline(requireContext()))
        getLocation()
    }
    private fun TextView.setAirQuality(index: Int) {
        this.apply {
            val ending: String = when (index) {
                1 -> {
                    this.setTextColor(Color.parseColor("#008000"))
                    "Good"
                }
                2 -> {
                    this.setTextColor(Color.parseColor("#FFFF00"))
                    "Moderate"
                }
                3 -> {
                    this.setTextColor(Color.parseColor("#FFA500"))
                    "Unhealthy for sensitive group"
                }
                4 -> {
                    this.setTextColor(Color.parseColor("#FF0000"))
                    "Unhealthy"
                }
                5 -> {
                    this.setTextColor(Color.parseColor("#800080"))
                    "Very Unhealthy"
                }
                else -> {
                    this.setTextColor(Color.parseColor("#4B0082"))
                    "Hazardous"
                }
            }
            text = "Air Quality: $ending"
        }
    }

    private fun setCards(list: ArrayList<ForecastCard>) {
        binding.recyclerView.run {
            if (adapter == null) {
                adapter = WeatherItemAdapter(requireContext())
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                isNestedScrollingEnabled = true
            }
            (binding.recyclerView.adapter as WeatherItemAdapter).setList(list)
        }
    }
    private fun checkPermissions(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val isLocationPermissonGranted = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!isLocationPermissonGranted)
        else {
            permissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        checkPermissions()
        val geocoder = Geocoder(requireContext(), Locale.US)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            lifecycleScope.launch {
                viewModel.getInfo(location?.latitude.toString(), location?.longitude.toString())
                val city = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
                binding.cityName.text = city[0].locality
            }
        }
    }
    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                if (isOnline(requireContext()))
                getLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                if (isOnline(requireContext()))
                getLocation()
            }
            else -> {
                Toast.makeText(
                    requireContext(),
                    "No permission granted to find location",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        private var mainFragment : MainFragment?= null
        fun newInstance(): MainFragment{
            if (mainFragment == null)
                mainFragment = MainFragment()
            return mainFragment as MainFragment
        }
    }
}