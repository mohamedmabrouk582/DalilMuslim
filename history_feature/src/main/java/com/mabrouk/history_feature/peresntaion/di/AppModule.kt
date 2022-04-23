package com.mabrouk.history_feature.peresntaion.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mabrouk.history_feature.BuildConfig
import com.mabrouk.history_feature.data.db.StoryApi
import com.mabrouk.history_feature.data.db.StoryDao
import com.mabrouk.history_feature.data.db.StoryDb
import com.mabrouk.history_feature.data.repository.StoryRepository
import com.mabrouk.history_feature.domain.usecase.StoryRepositoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getStoryApi(retrofit: Retrofit): StoryApi =
        retrofit.create(StoryApi::class.java)

    @Provides
    @Singleton
    fun getQuranRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


    @Provides
    @Singleton
    fun getStoryDao(@ApplicationContext context: Context): StoryDao =
        Room.databaseBuilder(context, StoryDb::class.java, "story.db").build().getDao()

    @Provides
    @Singleton
    fun getStoryUseCase(repository: StoryRepository) =
        StoryRepositoryUseCase(repository)

}