package com.mabrouk.core.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Singleton
class EventBus @Inject constructor() {

    private val downloadType =  MutableSharedFlow<String>()

    suspend fun sendType(name:String){
        downloadType.emit(name)
    }

    fun receiveType() : SharedFlow<String> {
        return downloadType.asSharedFlow()
    }

}