package com.mabrouk.prayertime.presentaion.workers

import com.mabrouk.core.R
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mabrouk.prayertime.presentaion.MASSAGE_KEY
import com.mabrouk.prayertime.presentaion.NOTIFICATION_ID
import com.mabrouk.prayertime.presentaion.view.SalatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.mabrouk.core.utils.NOTIFICATION_CHANNEL
import com.mabrouk.core.utils.createNotification
import com.mabrouk.prayertime.presentaion.IS_TOSHYEAH
import com.mabrouk.prayertime.presentaion.Twasheh_Fajar
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */
@HiltWorker
class PrayerSoundWorker @AssistedInject constructor(
    @Assisted appContext: Context, @Assisted params: WorkerParameters
) : CoroutineWorker(
    appContext, params
) {
    private val player by lazy { ExoPlayer.Builder(appContext).build() }

    override suspend fun doWork(): Result {
        val content = inputData.getString(MASSAGE_KEY) ?: "test"
        val tosheh = inputData.getBoolean(IS_TOSHYEAH, false)
        val twashehFajar = inputData.getBoolean(Twasheh_Fajar, false)
        CoroutineScope(Dispatchers.Main).launch {
            val sound = if (tosheh) Uri.parse("file:///android_asset/magrab_ramadan.mp3")
            else if (twashehFajar) Uri.parse("file:///android_asset/twasheh.mp3")
            else Uri.parse("file:///android_asset/prayer.mp3")
            val mediaItem = MediaItem.fromUri(sound)
            player.addMediaItem(mediaItem)

            if (!tosheh) player.addMediaItem(MediaItem.fromUri(Uri.parse("file:///android_asset/sharawy_doaa.mp3")))
            player.prepare()
            player.play()
            val manager = createNotification(
                applicationContext,
                NOTIFICATION_ID,
                NOTIFICATION_CHANNEL,
                applicationContext.getString(R.string.txt_prayer),
                content,
                createPendingIntent(applicationContext)
            ).apply {
                setUseFastForwardAction(false)
                setUseRewindAction(false)
                setUseNextAction(false)
                setUsePreviousAction(false)
            }

            manager.setPlayer(player)
            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == ExoPlayer.STATE_ENDED) {
                        manager.setPlayer(null)
                    }
                }
            })

        }
        return Result.success()
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, SalatActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}