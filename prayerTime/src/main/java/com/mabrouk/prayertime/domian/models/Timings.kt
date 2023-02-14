package com.mabrouk.prayertime.domian.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@Parcelize
data class Timings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Sunset: String,
    val Maghrib: String,
    val Isha: String,
    val Imsak: String,
    val Midnight: String,
    val Firstthird: String,
    val Lastthird: String,
) : Parcelable
