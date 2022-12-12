package com.example.weatherapplication.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapplication.database.SavedLocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, SavedLocationDatabase::class.java, "savedlocation")
        .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(db: SavedLocationDatabase) = db.getSavedLocationDao()
}