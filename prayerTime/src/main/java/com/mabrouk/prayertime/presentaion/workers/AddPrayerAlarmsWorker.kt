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
                val twasheh = context.getString(R.string.txt_twasheh)
                val salatTimings = context.resources.getStringArray(R.array.prayers)
                val content = prayer.meta.timezone
                prayer.timings.apply {
                    setAlarm(
                        context,
                        this.fajr,
                        "$twasheh ${salatTimings[0]} ($content)",
                        plusMinutes = -8,
                        twashehFajar = true
                    )
                    setAlarm(
                        context,
                        this.fajr,
                        "$header ${salatTimings[0]} ($content)",
                    )
                    setAlarm(
                        context, this.dhuhr, "$header ${salatTimings[1]} ($content)"
                    )

                    setAlarm(
                        context, this.asr, "$header ${salatTimings[2]} ($content)"
                    )

                    setAlarm(
                        context, this.maghrib, "$header ${salatTimings[3]} ($content)"
                    )
                    setAlarm(
                        context, this.isha, "$header ${salatTimings[4]} ($content)"
                    )
                    if (prayer.date.hijri.month.number == 9) {
                        setAlarm(
                            context,
                            this.maghrib,
                            "$twasheh ${salatTimings[3]} ($content)",
                            plusMinutes = -11,
                            tosheh = true
                        )
                        setAlarm(
                            context,
                            this.fajr,
                            "${context.getString(R.string.txt_sohor)} ${salatTimings[5]} ($content)",
                            -2
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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