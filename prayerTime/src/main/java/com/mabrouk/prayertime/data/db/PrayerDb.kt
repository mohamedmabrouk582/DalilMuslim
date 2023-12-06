package com.mabrouk.prayertime.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabrouk.prayertime.domian.models.CallingApi
import com.mabrouk.prayertime.domian.models.PrayerTiming

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */
@Database(entities = [PrayerTiming::class], version = 3, exportSchema = false)
@TypeConverters(PrayerConverter::class)
abstract class PrayerDb : RoomDatabase() {
    abstract fun getPrayerDao(): PrayerDao
}