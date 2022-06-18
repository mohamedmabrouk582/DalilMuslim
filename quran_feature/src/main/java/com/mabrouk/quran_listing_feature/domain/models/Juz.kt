package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Juz(
    @PrimaryKey
    val id: Int,
    @SerializedName("juz_number")
    val juzNumber: Int,
    @SerializedName("verse_mapping")
    val verseMapping: Map<String, String>? = null,
    var isDownload: Boolean = false
) : Parcelable