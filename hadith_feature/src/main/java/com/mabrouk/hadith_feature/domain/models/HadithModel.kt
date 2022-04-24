package com.mabrouk.hadith_feature.domain.models

import androidx.room.Entity

@Entity(primaryKeys = ["collection","bookNumber","hadithNumber"])
data class HadithModel(
    val collection:String,
    val bookNumber:String,
    val chapterId:String,
    val hadithNumber:String,
    val hadith:ArrayList<Hadith>
)