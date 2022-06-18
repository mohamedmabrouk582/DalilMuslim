package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Parcelize
data class Translation(
    val id:Long,
    @SerializedName("language_name")
    val languageName:String?,
    val textTranslation:String?,
    val textIndopak:String?,
    val verseKey:String?,
    val audioUrl:String?,
    val textTransliteration:String?
) : Parcelable
