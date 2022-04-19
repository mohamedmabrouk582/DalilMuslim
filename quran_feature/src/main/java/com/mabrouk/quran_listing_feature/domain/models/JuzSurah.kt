package com.mabrouk.quran_listing_feature.domain.models

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
data class JuzSurah(
    val juz_num:Int,
    val verse_ids:List<Int>,
    val verse_map:Map<String,String>?,
    val sura:Surah?=null,
    var from_to:String?=null,
    var isDownload:Boolean=false
)
