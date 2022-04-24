package com.mabrouk.azkar_feature.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mabrouk.azkar_feature.domain.models.Azkar

@Database(entities = [Azkar::class],version = 1,exportSchema = false)
abstract class AzkarDb : RoomDatabase(){
    abstract fun getAzkarDao() : AzkarDao
}