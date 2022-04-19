package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Surah(
    @PrimaryKey
    val id: Int,
    val bismillah_pre: Boolean = true,
    val revelation_place: String? = null,
    val name_complex: String? = null,
    val name_arabic: String? = null,
    val name_simple: String? = null,
    val verses_count: Int = 0,
    var from_to: String? = null,
    var isDownload: Boolean = false,
    var audiosDownloaded:Boolean=false,
    var tafsirDownloaded:Boolean=false
) : Parcelable
