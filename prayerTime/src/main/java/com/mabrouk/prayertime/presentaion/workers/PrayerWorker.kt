package com.mabrouk.prayertime.presentaion.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.mabrouk.prayertime.R
import com.mabrouk.prayertime.domian.models.CallingApi
import com.mabrouk.prayertime.domian.usecases.PrayerUseCases
import com.mabrouk.prayertime.presentaion.IS_TOSHYEAH
import com.mabrouk.prayertime.presentaion.LAT_KEY
import com.mabrouk.prayertime.presentaion.LONG_KEY
import com.mabrouk.prayertime.presentaion.MASSAGE_KEY
import com.mabrouk.prayertime.presentaion.PRAYER_ALARM_TAG
import com.mabrouk.prayertime.presentaion.SOUND_TAG
import com.mabrouk.prayertime.presentaion.Twasheh_Fajar
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.time.LocalDateTime
import java.util.TimeZone
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
        Log.d("testing work", "$lat : $long test")
        useCases.saveCallingApi(CallingApi(timing = DateTime.now().toString()))
        requestPrayerTimings(lat, long)
        createNotificationChannel(applicationContext)
        //showNotification(applicationContext)
        return Result.success()
    }

    val CHANNEL_ID = "CHANNEL_ID"
    val txt = DateTime.now(DateTimeZone.getDefault())

    private fun showNotification(context: Context) {
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(txt.toString())
            .setContentText(txt.toString("DD MM YYY"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Hello"))

        with(NotificationManagerCompat.from(context)){
            notify(1,builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = txt.toString()
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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

    private fun soundTask(
        context: Context,
        massage: String,
        tosheh: Boolean,
        twashehFajar: Boolean
    ) {
        val input =
            Data.Builder().putString(MASSAGE_KEY, massage).putBoolean(IS_TOSHYEAH, tosheh)
                .putBoolean(Twasheh_Fajar, twashehFajar).build()
        val build = OneTimeWorkRequest.Builder(PrayerSoundWorker::class.java)
            .addTag(SOUND_TAG)
            .setInputData(input)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(SOUND_TAG, ExistingWorkPolicy.KEEP, build)
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