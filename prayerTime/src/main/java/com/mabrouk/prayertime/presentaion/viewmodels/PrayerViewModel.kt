package com.mabrouk.prayertime.presentaion.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.mabrouk.prayertime.domian.alram.AlarmItem
import com.mabrouk.prayertime.presentaion.*
import com.mabrouk.prayertime.presentaion.workers.PrayerWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@HiltViewModel
class PrayerViewModel @Inject constructor() : ViewModel() {


    fun startService(context: Context, lat: Double, long: Double) {
        val putDouble =
            Data.Builder().putDouble(LAT_KEY, lat).putDouble(LONG_KEY, long).build()
        val build = PeriodicWorkRequest.Builder(
            PrayerWorker::class.java,
            1,
            TimeUnit.DAYS,
            1,
            TimeUnit.DAYS
        ).setInputData(putDouble)
            .build()

        val instance = WorkManager.getInstance(context)
        instance.enqueueUniquePeriodicWork("prayer", ExistingPeriodicWorkPolicy.KEEP, build)
    }
}