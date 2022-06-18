package com.mabrouk.quran_listing_feature.domain.models

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
data class JuzSurah(
    val juzNum:Int,
    val verseIds:List<Int>,
    val verseMap:Map<String,String>?,
    val sura:Surah?=null,
    var fromTo:String?=null,
    var isDownload:Boolean=false
)
