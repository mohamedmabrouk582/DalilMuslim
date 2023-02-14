package com.mabrouk.prayertime.domian.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@Parcelize
data class PrayerDate(
    val readable: String,
    val timestamp: Long,
    val gregorian: PrayerDateData,
    val hijri: PrayerDateData
) : Parcelable