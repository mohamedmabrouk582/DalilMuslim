package com.example.prayertime.domian.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
@Parcelize
@Entity
data class PrayerTiming(
    @PrimaryKey
    var dayDate:String,
    @Embedded
    val timings: Timings,
    @Embedded
    val date: PrayerDate,
    @Embedded
    val meta: PrayerMeta
) : Parcelable
