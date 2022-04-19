package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val url:String,
    val provider:String,
    val author_name:String
) : Parcelable
