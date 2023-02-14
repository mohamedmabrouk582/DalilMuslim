package com.mabrouk.prayertime.presentaion.di

import android.content.Context
import androidx.room.Room
import com.mabrouk.prayertime.data.db.PrayerDb
import com.mabrouk.prayertime.data.repository.PrayerRepository
import com.mabrouk.prayertime.domian.usecases.PrayerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getPrayerDao(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, PrayerDb::class.java, "PrayerTimings")
            .fallbackToDestructiveMigration()
            .build().getPrayerDao()

    @Singleton
    @Provides
    fun getRepository(repository: PrayerRepository) =
        PrayerUseCases(repository)
}