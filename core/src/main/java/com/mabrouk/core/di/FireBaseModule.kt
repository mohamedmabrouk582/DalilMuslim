package com.mabrouk.core.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {
    @Provides
    @Singleton
    fun getRemoteConfig(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance().apply {
            this.setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build()
            )
        }

}