package com.mabrouk.quran_listing_feature.presentation.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mabrouk.core.network.getBaseUrl
import com.mabrouk.core.network.getBaseUrlTafseer
import com.mabrouk.quran_listing_feature.data.api.QuranApi
import com.mabrouk.quran_listing_feature.data.api.TafseerApi
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
 * Copyright (c) 4/16/22
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun getTafseerApi(@Tafser retrofit: Retrofit) : TafseerApi =
        retrofit.create(TafseerApi::class.java)

    @Provides
    @Singleton
    fun getQuranApi(@Quran retrofit: Retrofit) : QuranApi =
        retrofit.create(QuranApi::class.java)

    @Provides
    @Tafser
    fun getTafserRetrofit(client : OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrlTafseer())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Quran
    fun getQuranRetrofit(client : OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
}