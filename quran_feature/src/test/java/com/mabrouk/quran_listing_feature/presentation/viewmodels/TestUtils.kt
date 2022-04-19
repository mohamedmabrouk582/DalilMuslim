package com.mabrouk.quran_listing_feature.presentation.viewmodels

import com.mabrouk.core.network.toArrayList
import com.mabrouk.quran_listing_feature.domain.models.*

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
object TestUtils {

    var savedJuz = arrayListOf<Juz>()
    var savedSurahs = arrayListOf<Surah>()
    var savedVerse = arrayListOf<Verse>()

    var readers = (1..20).map {
        QuranReader(it,"reader $it","")
    }.toArrayList()

    val juzs = (1..30).map {
        Juz(it,it)
    }.toArrayList()

    val surahs = (1 .. 114).map {
        Surah(it, name_arabic = "name$it")
    }.toArrayList()

    val verses = (1..200).map {
        Verse(it,(it%114)+1,(it%30)+1)
    }.toArrayList()

    var tafsirAya = TafsirAya(1,1)

}