package com.example.weatherapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.example.weatherapplication.isOnline
import com.example.weatherapplication.ui.main.MainFragment
import com.example.weatherapplication.ui.main.MainViewModel
import com.example.weatherapplication.ui.map.MapFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class MyApplication() : Application() {}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        viewModel.getLocations()
        viewModel.locations.observeForever {
            invalidateOptionsMenu()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        var i = 0
        menu.add(0, i++, Menu.NONE, "Add location")
        menu.add(0, i++, Menu.NONE, "Current location")
        if (!viewModel.locations.value.isNullOrEmpty())
            for (location in viewModel.locations.value!!) {
                menu.add(Menu.NONE, i++, Menu.NONE, location.name)
            }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.containerLayout, MapFragment().apply {
                        setViewModel(viewModel)
                    }).commit()
            }
            1 -> {
                checkPermissions()
            }
            else -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.containerLayout, MainFragment().apply {
                        setViewModel(viewModel)
                        arguments = Bundle().apply {
                            putSerializable(
                                "location_key",
                                viewModel.locations.value!![item.itemId - 2].key
                            )
                            putSerializable(
                                "location_name",
                                viewModel.locations.value!![item.itemId - 2].name
                            )
                        }
                    })
                    .commit()
            }
        }
        return true
    }

    private fun checkPermissions() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val isLocationPermissonGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (isLocationPermissonGranted)
            getLocation()
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
        if (isOnline(this)) {
            val geocoder = Geocoder(this, Locale.US)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val city = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)

                supportFragmentManager.beginTransaction()
                    .add(R.id.containerLayout, MainFragment().apply {
                        setViewModel(viewModel)
                        arguments = Bundle().apply {
                            putSerializable("lat", location.latitude)
                            putSerializable("lon", location.longitude)
                            putSerializable("location_name", city[0].locality)
                        }
                    }).commit()
            }
        } else setNoInternetConnection()
    }

    private fun setNoInternetConnection() {
        Toast.makeText(
            this,
            "No internet connection",
            Toast.LENGTH_LONG
        ).show()
        Log.d("Connection problems", "No internet connection.")
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