package com.mabrouk.core.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.mabrouk.core.network.getAudioUrl2
import com.mabrouk.core.utils.FileUtils.isFileBathFound
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit


/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */
enum class PrayerSounds(val type: String, val url: String) {
    MagrabRamadan(
        "magrab_ramadan",
        "https://firebasestorage.googleapis.com/v0/b/dalil-muslim-50dfb.appspot.com/o/magrab_ramadan.mp3?alt=media&token=2715e8c6-c9fa-480e-8fbb-fa71701bb344"
    ),
    Prayer(
        "prayer",
        "https://firebasestorage.googleapis.com/v0/b/dalil-muslim-50dfb.appspot.com/o/prayer.mp3?alt=media&token=f3b48abd-2739-46a8-aa75-fedfb763db14"
    ),
    SharawyDoaa(
        "sharawy_doaa",
        "https://firebasestorage.googleapis.com/v0/b/dalil-muslim-50dfb.appspot.com/o/sharawy_doaa.mp3?alt=media&token=f7704ac1-22c3-46d3-bba3-b4a21f76bbf6"
    ),
    TwashehFajar(
        "twasheh_fajar",
        "https://firebasestorage.googleapis.com/v0/b/dalil-muslim-50dfb.appspot.com/o/twasheh.mp3?alt=media&token=b3d320b1-a04d-4d5b-91e2-0307a735ecd5"
    )
}


fun downloadSound(context: Context, name: String, url: String) {
    Log.d("tdftft", name.isFileBathFound(context).toString())
    if (!name.isFileBathFound(context)) {
        val downloadManager = context.getSystemService(DownloadManager::class.java)
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_MUSIC, "$name.mp3")
        downloadManager.enqueue(request)
    }
}

fun downloadBasmalla(context: Context, url: String) {
    downloadSound(context, "$url/11", "${getAudioUrl2()}$url/11.mp3")
}

fun downloadAllSounds(context: Context) {
    downloadSound(context, PrayerSounds.Prayer.type, PrayerSounds.Prayer.url)
    downloadSound(context, PrayerSounds.MagrabRamadan.type, PrayerSounds.MagrabRamadan.url)
    downloadSound(context, PrayerSounds.SharawyDoaa.type, PrayerSounds.SharawyDoaa.url)
    downloadSound(context, PrayerSounds.TwashehFajar.type, PrayerSounds.TwashehFajar.url)
}

fun getCurrentDate(pattern: String = "dd-MM-yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(LocalDateTime.now())
}

fun calculateInitialDelay(numDays: Long,hour: Int): Long {
    val currentTimeMillis = System.currentTimeMillis()
    val desiredTimeMillis = getDesiredTimeMillis(hour)
    var initialDelay = desiredTimeMillis - currentTimeMillis
    if (initialDelay < 0) {
        initialDelay += TimeUnit.DAYS.toMillis(numDays)
    }
    return initialDelay
}

fun getDesiredTimeMillis(hour: Int): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        set(Calendar.HOUR_OF_DAY, 16)
        set(Calendar.MINUTE, 36)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun getDate(date: LocalDateTime, pattern: String = "dd-MM-yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(date)
}