package com.example.prayertime.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prayertime.domian.models.PrayerTiming
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */
@Dao
interface PrayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrayerTimings(list: List<PrayerTiming>): List<Long>

    @Query("select * from PrayerTiming")
    fun getSavedPrayerTimings(): Flow<List<PrayerTiming>>

    @Query("select * from prayertiming where dayDate=:date")
    fun getSavedPrayerTimingsByDate(date: String): Flow<PrayerTiming>

    @Query("Delete from prayertiming")
    suspend fun clearDb()

}