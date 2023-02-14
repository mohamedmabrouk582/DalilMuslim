package com.example.prayertime.domian.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@Parcelize
data class PrayerMeta(
    val latitude: Double,
    val longitude: Double,
    val timezone: String
) : Parcelable
