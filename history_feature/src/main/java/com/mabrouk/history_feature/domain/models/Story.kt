package com.mabrouk.history_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@Entity
@Parcelize
data class Story(
    @SerializedName("video_key")
    @PrimaryKey
    var videoKey:String,
    var url:String,
    var title:String,
    @SerializedName("thumbnail_url")
    var getThumbUrl:String,
    val ext : String = "mp4",
    var suraId: Int? = 0,
    var videoId:Int = 0,
    var isPlaying:Boolean = false
) : Parcelable