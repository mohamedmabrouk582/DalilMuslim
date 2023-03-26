package com.mabrouk.prayertime.presentaion.workers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mabrouk.prayertime.presentaion.MASSAGE_KEY
import com.mabrouk.prayertime.presentaion.SOUND_TAG
import com.mabrouk.prayertime.presentaion.view.SalatActivity
import com.mabrouk.core.utils.NOTIFICATION_CHANNEL
import com.mabrouk.prayertime.presentaion.IS_TOSHYEAH
import com.mabrouk.prayertime.presentaion.Twasheh_Fajar


/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
class PrayerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.apply {
            val massage = intent?.extras?.getString(NOTIFICATION_CHANNEL) ?: "tee"
            val tosheh = intent?.extras?.getBoolean(IS_TOSHYEAH) ?: false
            val twashehFajar = intent?.extras?.getBoolean(Twasheh_Fajar) ?: false
            soundTask(this, massage, tosheh, twashehFajar = twashehFajar)
            Intent(context, SalatActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
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

}