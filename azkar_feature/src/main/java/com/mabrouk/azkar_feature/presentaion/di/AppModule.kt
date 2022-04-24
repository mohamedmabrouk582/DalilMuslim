package com.mabrouk.azkar_feature.presentaion.di

import android.content.Context
import androidx.room.Room
import com.mabrouk.azkar_feature.data.db.AzkarDao
import com.mabrouk.azkar_feature.data.db.AzkarDb
import com.mabrouk.azkar_feature.data.repository.AzkarRepository
import com.mabrouk.azkar_feature.domain.usecases.AzkarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getDao(@ApplicationContext context: Context) : AzkarDao =
        Room.databaseBuilder(context, AzkarDb::class.java, "azkar-db")
            .createFromAsset("azkar-db")
            .build().getAzkarDao()

    @Provides
    @Singleton
    fun getRepository( repo : AzkarRepository) =
        AzkarUseCase(repo)
}