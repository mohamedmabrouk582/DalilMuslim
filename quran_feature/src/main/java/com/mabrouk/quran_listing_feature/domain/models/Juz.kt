package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Juz(
    @PrimaryKey
    val id: Int,
    val juz_number: Int,
    val verse_mapping: Map<String, String>? = null,
    var isDownload: Boolean = false
) : Parcelable