package com.example.weatherapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.example.weatherapplication.setAnimatedIcon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@HiltAndroidApp
class MyApplication() : Application() {}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val isLocationPermissonGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (isLocationPermissonGranted) getLocation()
        else {
            permissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        viewModel.info.observeForever {
            binding.apply {
                progressBar.visibility = View.GONE
                temperature.text = it.main.temp.roundToInt().toString() + "\u00B0C"
                text.text = it.toString()
                icon.setAnimatedIcon(it.weather[0].main,it.weather[0].description)
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        val geocoder = Geocoder(this)
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
                getLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getLocation()
            }
            else -> {
                Toast.makeText(
                    this,
                    "No permission granted to find location",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}