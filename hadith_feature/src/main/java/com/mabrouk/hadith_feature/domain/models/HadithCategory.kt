package com.mabrouk.hadith_feature.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HadithCategory(
    @PrimaryKey
    val name:String,
    val hasBooks:Boolean,
    val hasChapters:Boolean,
    val totalHadith:Int,
    val totalAvailableHadith:Int,
    val collection:ArrayList<HadithCollection>
)