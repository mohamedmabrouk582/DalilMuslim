package com.mabrouk.prayertime.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.mabrouk.prayertime.domian.alram.AlarmItem
import com.mabrouk.prayertime.domian.alram.AlarmScheduler
import com.mabrouk.prayertime.presentaion.workers.PrayerReceiver
import com.mabrouk.core.utils.NOTIFICATION_CHANNEL
import com.mabrouk.prayertime.presentaion.IS_TOSHYEAH
import com.mabrouk.prayertime.presentaion.Twasheh_Fajar
import java.time.ZoneId

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */

class AlarmSchedulerManager(val context: Context) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun alarmSchedule(item: AlarmItem) {
        checkPermission()
        val intent = Intent(context, PrayerReceiver::class.java).apply {
            putExtra(NOTIFICATION_CHANNEL, item.massage)
            putExtra(IS_TOSHYEAH,item.tosheh)
            putExtra(Twasheh_Fajar,item.TwashehFajar)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context, item.hashCode(), Intent(context, PrayerReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}