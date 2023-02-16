package com.mabrouk.prayertime.presentaion.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.mabrouk.prayertime.domian.alram.AlarmItem
import com.mabrouk.prayertime.domian.usecases.PrayerUseCases
import com.mabrouk.core.utils.getCurrentDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import com.mabrouk.core.R

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/10/23
 */
@HiltWorker
class AddPrayerAlarmsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val useCases: PrayerUseCases
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        getSavedPrayerTimings(applicationContext)
        return Result.success()
    }

    private suspend fun getSavedPrayerTimings(context: Context) {
        useCases.getSavedTimingsByDay(getCurrentDate()).let { prayer ->
            try {
                val header = context.getString(R.string.txt_salat)
                val salatTimings = context.resources.getStringArray(R.array.prayers)
                val content = prayer.meta.timezone
                prayer.timings.apply {
                    setAlarm(
                        context,
                        this.fajr,
                        "$header ${salatTimings[0]} ($content)"
                    )
                    setAlarm(
                        context,
                        this.dhuhr,
                        "$header ${salatTimings[1]} ($content)"
                    )

                    setAlarm(
                        context, this.asr,
                        "$header ${salatTimings[2]} ($content)"
                    )
                    setAlarm(
                        context, this.maghrib,
                        "$header ${salatTimings[3]} ($content)"
                    )
                    setAlarm(
                        context, this.isha,
                        "$header ${salatTimings[4]} ($content)"
                    )
                    if (prayer.date.hijri.month.number == 9) {
                        setAlarm(
                            context, this.lastthird,
                            "${context.getString(R.string.txt_sohor)} ${salatTimings[4]} ($content)"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setAlarm(context: Context, time: String, massage: String) {
        val split = time.split(":")
        val hour = split[0].toInt()
        val min = split[1].split(" ")[0].toInt()
        val alarm = com.mabrouk.prayertime.data.alarm.AlarmSchedulerManager(context)
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