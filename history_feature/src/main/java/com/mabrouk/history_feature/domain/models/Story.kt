package com.mabrouk.history_feature.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@Entity
@Parcelize
data class Story(
    @PrimaryKey
    val video_key:String ,
    var url:String,
    var title:String,
    var getThumbUrl:String,
    val ext : String = "mp4",
    var suraId: Int? = 0,
    var videoId:Int = 0,
    var isPlaying:Boolean = false
) : Parcelable