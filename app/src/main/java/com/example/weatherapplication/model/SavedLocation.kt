package com.example.weatherapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["city_name"], unique = true)])
data class SavedLocation(
    @PrimaryKey (autoGenerate = true)
    val key : Long,
    @ColumnInfo (name = "city_lat")
    val lat: Double,
    @ColumnInfo (name = "city_lon")
    val lon: Double,
    @ColumnInfo(name = "city_name")
    val name: String)
