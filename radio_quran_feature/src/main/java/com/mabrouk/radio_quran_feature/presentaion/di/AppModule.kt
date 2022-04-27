package com.mabrouk.radio_quran_feature.presentaion.di

import android.content.Context
import androidx.room.Room
import com.mabrouk.radio_quran_feature.data.api.RadioApi
import com.mabrouk.radio_quran_feature.data.db.RadioDao
import com.mabrouk.radio_quran_feature.data.db.RadioDb
import com.mabrouk.radio_quran_feature.data.repository.RadioRepository
import com.mabrouk.radio_quran_feature.domain.usecases.RadioUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getRadioApi(retrofit: Retrofit) : RadioApi =
        retrofit.create(RadioApi::class.java)

    @Provides
    @Singleton
    fun getRadioUseCase(repository: RadioRepository): RadioUseCase =
        RadioUseCase(repository)

    @Provides
    @Singleton
    fun getRadioDao(@ApplicationContext context: Context): RadioDao =
        Room.databaseBuilder(context, RadioDb::class.java, "radio.db").build().getDao()
}