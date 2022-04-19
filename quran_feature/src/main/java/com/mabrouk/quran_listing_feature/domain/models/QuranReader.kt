package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Entity
@Parcelize
data class QuranReader(
    @PrimaryKey
    @SerializedName("id")
    val readerId: Int,
    val name: String,
    val sufix: String,
    var versesIds: ArrayList<Long>? = arrayListOf()

) : Parcelable {
    @Ignore
    var isSelected: Boolean = false
}
