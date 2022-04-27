package com.mabrouk.radio_quran_feature.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mabrouk.radio_quran_feature.domain.models.Radio

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
@Database(entities = [Radio::class] , version = 1 , exportSchema = true)
abstract class RadioDb : RoomDatabase(){
    abstract fun getDao() : RadioDao
}