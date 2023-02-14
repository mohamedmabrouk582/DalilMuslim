package com.example.prayertime.domian.repository

import com.example.prayertime.domian.models.PrayerResponse
import com.example.prayertime.domian.models.PrayerTiming
import com.mabrouk.core.network.Result
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
interface PrayerDefaultRepository {
    fun getPrayerTiming(lat: Double, lang: Double, month: Int, year: Int) : Flow<Result<PrayerResponse>>
    suspend fun savePrayerTimings(timings: List<PrayerTiming>): List<Long>
    suspend fun getSavedTimings(): ArrayList<PrayerTiming>
    suspend fun getSavedTimingsByDay(day:String):PrayerTiming
    suspend fun deleteAllPrayerTimings()
}