package com.mabrouk.core.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @IoDispatcher
    @Provides
    fun getIoDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun flipperNetworkInterceptor(plugin: NetworkFlipperPlugin) =
        FlipperOkhttpInterceptor(plugin)

    @Provides
    @Singleton
    fun getNetworkPlugin()  = NetworkFlipperPlugin()
}