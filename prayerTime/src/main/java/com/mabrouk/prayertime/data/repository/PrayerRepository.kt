package com.mabrouk.prayertime.data.repository

import android.content.Context
import com.mabrouk.prayertime.data.api.PrayerApi
import com.mabrouk.prayertime.data.db.PrayerDao
import com.mabrouk.prayertime.domian.models.PrayerResponse
import com.mabrouk.prayertime.domian.models.PrayerTiming
import com.mabrouk.prayertime.domian.repository.PrayerDefaultRepository
import com.mabrouk.core.network.Result
import com.mabrouk.core.network.executeCall2
import com.mabrouk.core.network.toArrayList
import com.mabrouk.prayertime.domian.models.CallingApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */

class PrayerRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val api: PrayerApi,
    private val prayerDao: PrayerDao
) : PrayerDefaultRepository {
    override fun getPrayerTiming(
        lat: Double,
        lang: Double,
        month: Int,
        year: Int
    ): Flow<Result<PrayerResponse>> {
        return executeCall2(context) {
            api.getPrayerTimings(lat, lang, month, year)
        }
    }

    override suspend fun savePrayerTimings(timings: List<PrayerTiming>): List<Long> {
        return prayerDao.insertPrayerTimings(timings)
    }

    override suspend fun getSavedTimings(): ArrayList<PrayerTiming> {
        return prayerDao.getSavedPrayerTimings().first().toArrayList()
    }

    override suspend fun getSavedTimingsByDay(day: String): PrayerTiming {
        return prayerDao.getSavedPrayerTimingsByDate(day).first()
    }

    override suspend fun deleteAllPrayerTimings() {
        prayerDao.clearDb()
    }
}