package com.example.weatherapplication.ui.map

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapplication.databinding.FragmentMapBinding
import com.example.weatherapplication.ui.main.MainViewModel
import com.example.weatherapplication.ui.map.dialog.AddDialogFragment
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.gestures.gestures
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var mapView: MapView? = null
    private lateinit var binding: FragmentMapBinding
    private lateinit var vm : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        mapView?.getMapboxMap()!!.setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView!!.gestures.apply {
            pitchEnabled = false
            addOnMapLongClickListener() {
                val geocoder = Geocoder(requireContext(), Locale.US)
                val city = geocoder.getFromLocation(it.latitude(), it.longitude(), 1)
                val dialog = AddDialogFragment()
                dialog.apply {
                    arguments = Bundle().apply {
                        if (city[0].locality == null)
                            putSerializable("addCity", city[0].featureName)
                        else
                            putSerializable("addCity", city[0].locality)
                        putSerializable("addCityLat", it.latitude())
                        putSerializable("addCityLon", it.longitude())
                    }
                    setVM(vm)
                }.show(parentFragmentManager, "")
                activity?.invalidateOptionsMenu()
                true
            }
        }
    }

    fun setViewModel(_vm:MainViewModel){
        vm = _vm
    }

    companion object {
        private var fragmentMap: MapFragment? = null
    }
}