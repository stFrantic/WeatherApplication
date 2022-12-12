package com.example.weatherapplication.ui.map.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.weatherapplication.databinding.FragmentAddDialogBinding
import com.example.weatherapplication.model.SavedLocation
import com.example.weatherapplication.ui.main.MainViewModel

class AddDialogFragment : DialogFragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = FragmentAddDialogBinding.inflate(layoutInflater)
        val cityName = arguments?.getSerializable("addCity") as String
        val cityLat = arguments?.getSerializable("addCityLat") as Double
        val cityLon = arguments?.getSerializable("addCityLon") as Double

        view.cityName.text = cityName

        return AlertDialog.Builder(requireContext())
            .setView(view.root)
            .setTitle("Do you want to track this location?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                val resault = viewModel.saveLocation(SavedLocation(0, cityLat, cityLon, cityName))
                when (resault) {
                    true -> {
                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Location Saved",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        activity?.invalidateOptionsMenu()
                    }
                    false ->
                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "This location is already saved",
                                Toast.LENGTH_LONG

                            ).show()
                        }
                }

                dismiss()
            }
            .setNegativeButton("No") { _, _ ->
                dismiss()
            }
            .create()
    }

    fun setVM(vm: MainViewModel) {
        viewModel = vm
    }
}