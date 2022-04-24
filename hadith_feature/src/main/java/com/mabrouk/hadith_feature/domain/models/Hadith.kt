package com.mabrouk.hadith_feature.domain.models

data class Hadith (
    val lang:String,
    val chapterNumber:String,
    val chapterTitle:String,
    val urn:Long,
    val body:String
    )