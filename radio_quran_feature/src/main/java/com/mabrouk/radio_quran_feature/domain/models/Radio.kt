package com.mabrouk.radio_quran_feature.domain.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
@Entity
data class Radio (
    @NonNull
    @PrimaryKey
    val name:String,
    val radio_url : String
    )