package com.mabrouk.quran_listing_feature.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mabrouk.quran_listing_feature.data.api.QuranApi
import com.mabrouk.quran_listing_feature.data.api.TafseerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/18/22
 */
@Module
@InstallIn(SingletonComponent::class)
class TestApiModule {

    @Provides
    @Named("test_api")
    fun getApi() : QuranApi =
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(QuranApi::class.java)

    @Provides
    @Named("test_tafseer_api")
    fun getTafsserApi() : TafseerApi =
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(TafseerApi::class.java)
}