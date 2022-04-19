package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */

@Parcelize
data class Audio(
    val url:String,
    val duration:Int,
    val format:String
) : Parcelable
