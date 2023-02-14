package com.mabrouk.prayertime.domian.models

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@Parcelize
data class PrayerDateData(
    val date: String,
    val format: String,
    val day: Int,
    val year: Int,
    val weekday: HashMap<String, String>,
    @Embedded
    val month: PrayerMonth
) : Parcelable

@Parcelize
data class PrayerMonth(
    val number: Int,
    val en: String?,
    val ar: String?
) : Parcelable

