package com.mabrouk.hadith_feature.presentaion.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mabrouk.core.network.getApiKey
import com.mabrouk.core.network.getBaseUrlSunnah
import com.mabrouk.hadith_feature.data.api.HadithApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun getHadithApi(@Hadith retrofit: Retrofit): HadithApi =
        retrofit.create(HadithApi::class.java)

    @Provides
    @Singleton
    @Hadith
    fun getRetrofitSunnah(@Hadith client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrlSunnah())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Provides
    @Singleton
    @Hadith
    fun getClients(
        interceptor: HttpLoggingInterceptor,
        flipperOkhttpInterceptor : FlipperOkhttpInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(flipperOkhttpInterceptor)
            .addInterceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("X-API-Key", getApiKey()).build()
                return@addInterceptor chain.proceed(request)
            }
            .build()

}