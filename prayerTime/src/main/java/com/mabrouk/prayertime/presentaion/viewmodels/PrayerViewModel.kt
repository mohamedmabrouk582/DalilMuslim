package com.mabrouk.prayertime.presentaion.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.mabrouk.prayertime.presentaion.*
import com.mabrouk.prayertime.presentaion.workers.PrayerWorker
import dagger.hilt.android.lifecycle.HiltViewModel
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
        Log.d("testing work", "$lat : $long")

        val build = PeriodicWorkRequest.Builder(
            PrayerWorker::class.java,
            20,
            TimeUnit.MINUTES,
        ).setInputData(putDouble).build()

        val instance = WorkManager.getInstance(context)

        instance.enqueueUniquePeriodicWork("prayer", ExistingPeriodicWorkPolicy.REPLACE, build)
    }
}