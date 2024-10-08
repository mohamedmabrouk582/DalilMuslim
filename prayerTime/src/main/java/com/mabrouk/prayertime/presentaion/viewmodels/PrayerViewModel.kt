package com.mabrouk.prayertime.presentaion.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.mabrouk.core.utils.calculateInitialDelay
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
            TimeUnit.HOURS
        ).setInputData(putDouble)
            .setInitialDelay(calculateInitialDelay(1, 11), TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                1,
                TimeUnit.HOURS
            )
            .build()

        val instance = WorkManager.getInstance(context)
        instance.enqueueUniquePeriodicWork("prayer", ExistingPeriodicWorkPolicy.KEEP, build)
    }

    private fun setAlarm(
        context: Context,
        time: String,
        massage: String,
        plusHours: Int = 0,
        plusMinutes: Int = 0,
        tosheh: Boolean = false,
        twashehFajar: Boolean = false
    ) {
        val split = time.split(":")
        val hour = split[0].toInt() + plusHours
        val min = split[1].split(" ")[0].toInt() + plusMinutes
        val alarm = com.mabrouk.prayertime.data.alarm.AlarmSchedulerManager(context)
        val current = LocalDateTime.now()
        val alarmTime = LocalDateTime.of(current.year, current.month, current.dayOfMonth, hour, min)

        if (alarmTime.isAfter(current)) alarm.alarmSchedule(
            AlarmItem(
                alarmTime, massage, tosheh, twashehFajar
            )
        )
    }
}