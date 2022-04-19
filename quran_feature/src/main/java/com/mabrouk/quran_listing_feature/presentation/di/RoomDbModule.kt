package com.mabrouk.quran_listing_feature.presentation.di

import android.content.Context
import androidx.room.Room
import com.mabrouk.quran_listing_feature.data.db.QuranDB
import com.mabrouk.quran_listing_feature.data.db.QuranDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Module
@InstallIn(SingletonComponent::class)
class RoomDbModule {

    @Provides
    @Singleton
    fun getQuranDao(@ApplicationContext context : Context) : QuranDao =
        Room.databaseBuilder(context,QuranDB::class.java,"quran.db")
            .build().getQuranDao()

}