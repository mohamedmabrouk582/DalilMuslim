package com.mabrouk.hadith_feature.presentaion.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mabrouk.hadith_feature.BuildConfig
import com.mabrouk.hadith_feature.data.api.HadithApi
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            .baseUrl(BuildConfig.Base_Url_sunnah)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Provides
    @Singleton
    @Hadith
    fun getClients(
        interceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(ChuckInterceptor(context))
            .addInterceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("X-API-Key", BuildConfig.API_KEY).build()
                return@addInterceptor chain.proceed(request)
            }
            .build()

}