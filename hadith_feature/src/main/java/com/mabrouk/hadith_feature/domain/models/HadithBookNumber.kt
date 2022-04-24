package com.mabrouk.hadith_feature.domain.models

import androidx.room.Entity

@Entity(primaryKeys = ["collectionName","bookNumber"])
data class HadithBookNumber(
    var collectionName:String = "",
    val bookNumber:String,
    val hadithStartNumber:Int,
    val hadithEndNumber:Int,
    val numberOfHadith:Int,
    val book:ArrayList<HadithBook>
)