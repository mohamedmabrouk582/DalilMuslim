package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Entity
@Parcelize
data class TafsirAya(
    @SerializedName("tafseer_id")
    val tafseerId: Int,
    @SerializedName("ayah_number")
    val verse_id: Int,
    @PrimaryKey
    val text: String = "",
    val language_name: String? = null,
    @SerializedName("tafseer_name")
    val resource_name: String? = null,
    @SerializedName("ayah_url")
    val verse_key: String = "/quran/1/1/"
) : Parcelable {
    fun getChapter() = verse_key.replace("/quran/","").split("/")[0].toInt()
}
