package com.example.weatherapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapplication.model.SavedLocation

@Dao
interface SavedLocationDAO {
    @Insert
    suspend fun insertLocation(location: SavedLocation)

    @Query("select * from savedlocation")
    suspend fun getAllLocations(): List<SavedLocation>

    @Delete
    suspend fun deleteLocation(location: SavedLocation)

    @Query("select * from savedlocation where :key == `key`")
    suspend fun getLocationByKey(key:Long) : SavedLocation

}