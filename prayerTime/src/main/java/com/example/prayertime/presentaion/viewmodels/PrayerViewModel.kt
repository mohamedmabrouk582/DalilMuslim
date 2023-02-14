package com.example.prayertime.presentaion.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.prayertime.data.alarm.AlarmSchedulerManager
import com.example.prayertime.domian.alram.AlarmItem
import com.example.prayertime.presentaion.*
import com.example.prayertime.presentaion.workers.PrayerSoundWorker
import com.example.prayertime.presentaion.workers.PrayerWorker
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


    fun setAlarm(context: Context, time: String, massage: String) {
        val split = time.split(":")
        val hour = split[0].toInt()
        val min = split[1].split(" ")[0].toInt()
        val alarm = AlarmSchedulerManager(context)
        val current = LocalDateTime.now()
        val alarmTime = LocalDateTime.of(current.year, current.month, current.dayOfMonth, hour, min)

        if (alarmTime.isAfter(current))
            alarm.alarmSchedule(
                AlarmItem(
                    alarmTime,
                    massage
                )
            )
    }


}