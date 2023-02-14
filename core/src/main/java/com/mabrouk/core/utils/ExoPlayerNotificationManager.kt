package com.mabrouk.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.mabrouk.core.R

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/12/23
 */

const val NOTIFICATION_CHANNEL = "test"


fun createNotification(
    context: Context,
    notificationId: Int,
    channelId: String,
    title: String,
    content: String,
    pendingIntent: PendingIntent
): PlayerNotificationManager {
    val notificationManager =
        context.getSystemService(NotificationManager::class.java)
    val channel =
        NotificationChannel(
            NOTIFICATION_CHANNEL,
            "testing",
            NotificationManager.IMPORTANCE_HIGH
        )
    notificationManager.createNotificationChannel(channel)
    val builder = PlayerNotificationManager.Builder(context, notificationId, channelId)
        .setMediaDescriptionAdapter(
            getMediaDescriptorInstance(
                context,
                title,
                content,
                pendingIntent
            )
        ).setSmallIconResourceId(R.drawable.logo)
    return builder.build()
}

fun getMediaDescriptorInstance(
    context: Context,
    title: String,
    content: String,
    pendingIntent: PendingIntent
) = object : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
        return title
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return pendingIntent
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return content
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, R.drawable.salat)
    }

}