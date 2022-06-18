package com.mabrouk.quran_listing_feature.presentation.di

import com.mabrouk.quran_listing_feature.data.repository.AyaRepository
import com.mabrouk.quran_listing_feature.data.repository.QuranRepository
import com.mabrouk.quran_listing_feature.domain.usecases.AyaRepositoryUseCases
import com.mabrouk.quran_listing_feature.domain.usecases.QuranRepositoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun getQuranUseCases(repository: QuranRepository) =
        QuranRepositoryUseCase(repository)

    @Provides
    @Singleton
    fun getAyaUseCases(repository: AyaRepository) =
        AyaRepositoryUseCases(repository)
}