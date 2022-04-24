package com.mabrouk.hadith_feature.presentaion.di

import android.content.Context
import androidx.room.Room
import com.mabrouk.hadith_feature.data.db.HadithDao
import com.mabrouk.hadith_feature.data.db.HadithDb
import com.mabrouk.hadith_feature.data.repository.HadithRepository
import com.mabrouk.hadith_feature.domain.usecases.HadithRepositoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getRepositoryUseCase(repository: HadithRepository) =
        HadithRepositoryUseCase(repository)

    @Provides
    @Singleton
    fun getHadithDao(@ApplicationContext context: Context): HadithDao =
        Room.databaseBuilder(context, HadithDb::class.java, "hadithDb")
            .fallbackToDestructiveMigration()
            .build().getHadithDao()
}