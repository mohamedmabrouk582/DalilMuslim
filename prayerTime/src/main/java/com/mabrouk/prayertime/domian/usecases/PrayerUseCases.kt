package com.mabrouk.prayertime.domian.usecases

import com.mabrouk.prayertime.domian.models.PrayerResponse
import com.mabrouk.prayertime.domian.models.PrayerTiming
import com.mabrouk.prayertime.domian.repository.PrayerDefaultRepository
import com.mabrouk.core.network.Result
import com.mabrouk.prayertime.domian.models.CallingApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
class PrayerUseCases @Inject constructor(val repository: PrayerDefaultRepository) {
    fun getPrayerTiming(
        lat: Double,
        lang: Double,
        month: Int,
        year: Int
    ): Flow<Result<PrayerResponse>> {
        return repository.getPrayerTiming(lat, lang, month, year)
    }

    suspend fun savePrayerTimings(timings: List<PrayerTiming>): List<Long> {
        return repository.savePrayerTimings(timings)
    }

    suspend fun getSavedTimings(): ArrayList<PrayerTiming> {
        return repository.getSavedTimings()
    }

    suspend fun getSavedTimingsByDay(day:String):PrayerTiming{
        return repository.getSavedTimingsByDay(day)
    }

    suspend fun deleteAllPrayerTimings(){
        return repository.deleteAllPrayerTimings()
    }

}