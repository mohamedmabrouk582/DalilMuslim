package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Surah(
    @PrimaryKey
    val id: Int,
    @SerializedName("bismillah_pre")
    val bismillahPre: Boolean = true,
    @SerializedName("revelation_place")
    val revelationPlace: String? = null,
    @SerializedName("name_complex")
    val nameComplex: String? = null,
    @SerializedName("name_arabic")
    val nameArabic: String? = null,
    @SerializedName("name_simple")
    val nameSimple: String? = null,
    @SerializedName("verses_count")
    val versesCount: Int = 0,
    @SerializedName("from_to")
    var fromTo: String? = null,
    var isDownload: Boolean = false,
    var audiosDownloaded:Boolean=false,
    var tafsirDownloaded:Boolean=false
) : Parcelable
