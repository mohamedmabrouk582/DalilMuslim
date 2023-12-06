package com.mabrouk.prayertime.presentaion.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.mabrouk.prayertime.domian.usecases.PrayerUseCases
import com.mabrouk.prayertime.presentaion.LAT_KEY
import com.mabrouk.prayertime.presentaion.LONG_KEY
import com.mabrouk.prayertime.presentaion.PRAYER_ALARM_TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@HiltWorker
class PrayerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val useCases: PrayerUseCases
) : CoroutineWorker(
    appContext,
    params
) {

    override suspend fun doWork(): Result {
        val lat = inputData.getDouble(LAT_KEY, 0.0)
        val long = inputData.getDouble(LONG_KEY, 0.0)
        requestPrayerTimings(lat, long)
        return Result.success()
    }

    private suspend fun requestPrayerTimings(lat: Double, long: Double) {
        val currentTime = LocalDateTime.now()
        val month = currentTime.month?.value
        val year = currentTime.year
        useCases.getPrayerTiming(lat, long, month ?: 1, year).collect {
            if (it is com.mabrouk.core.network.Result.OnSuccess) {
                useCases.deleteAllPrayerTimings()
                useCases.savePrayerTimings(
                    it.data.data.map { time ->
                        time.dayDate = time.date.gregorian.date
                        time
                    }
                )
                alarmTask()
            }
        }
    }

    private fun alarmTask() {
        val build = PeriodicWorkRequest.Builder(
            AddPrayerAlarmsWorker::class.java,
            15,
            TimeUnit.MINUTES,
            15,
            TimeUnit.MINUTES
        ).addTag(PRAYER_ALARM_TAG).build()


        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                PRAYER_ALARM_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                build
            )
    }

}