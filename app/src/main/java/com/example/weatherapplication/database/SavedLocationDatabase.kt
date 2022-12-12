package com.example.weatherapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapplication.model.SavedLocation


@Database(
    entities = [SavedLocation::class],
    version = 1
)
abstract class SavedLocationDatabase : RoomDatabase() {
    abstract fun getSavedLocationDao(): SavedLocationDAO
}