package com.mabrouk.quran_listing_feature.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mabrouk.quran_listing_feature.data.api.QuranApi
import com.mabrouk.quran_listing_feature.data.db.QuranDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/18/22
 */
@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Named("test_db")
    fun getDB(@ApplicationContext context: Context): QuranDB =
        Room.inMemoryDatabaseBuilder(context, QuranDB::class.java).allowMainThreadQueries()
            .build()
}