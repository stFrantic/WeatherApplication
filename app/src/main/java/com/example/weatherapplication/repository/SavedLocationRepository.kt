package com.example.weatherapplication.repository

import com.example.weatherapplication.database.SavedLocationDAO
import com.example.weatherapplication.model.SavedLocation
import javax.inject.Inject

class SavedLocationRepository @Inject constructor(private val locationsDao: SavedLocationDAO) {

    suspend fun getLocations() = locationsDao.getAllLocations()

    suspend fun addLocation(location: SavedLocation) = locationsDao.insertLocation(location)

    suspend fun getLocationByKey(key:Long) = locationsDao.getLocationByKey(key)
}