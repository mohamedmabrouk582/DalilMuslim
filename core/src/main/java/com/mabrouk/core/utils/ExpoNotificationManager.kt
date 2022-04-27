package com.mabrouk.core.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.mabrouk.core.R

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */

class DescriptionAdapter(val reader:String , val context: Context, val activity : Class<*>) : PlayerNotificationManager.MediaDescriptionAdapter{

    override fun getCurrentContentTitle(player: Player): CharSequence {
      return "Aya ${player.currentMediaItem?.mediaId}"
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val intent = Intent(context, activity::class.java)
        intent.putExtra(VERSES_ID,player.currentMediaItem?.mediaId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return "$reader Aya ${player.currentMediaItem?.mediaId}"
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        val bitmap: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.download)
//        Glide.with(context)
//            .asBitmap()
//            .load(items[player.currentWindowIndex].getThumbUrl)
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    bitmap = resource
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.download)
//                }
//
//            })
        return bitmap
    }

}

const val VERSES_ID = "VERSES_ID"