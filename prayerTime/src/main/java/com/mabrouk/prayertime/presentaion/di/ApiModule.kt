package com.mabrouk.prayertime.presentaion.di

import com.mabrouk.prayertime.data.api.PrayerApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mabrouk.core.network.getPreryTime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun getPrayerApi(@Prayer retrofit: Retrofit): PrayerApi =
        retrofit.create(PrayerApi::class.java)

    @Prayer
    @Provides
    fun getPrayerRetrofit(client: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(getPreryTime())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
}