package com.mabrouk.quran_listing_feature.presentation.utils

import android.util.Log
import com.mabrouk.quran_listing_feature.domain.models.Juz
import com.mabrouk.quran_listing_feature.domain.models.JuzSurah
import com.mabrouk.quran_listing_feature.domain.models.Surah

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
const val SURAH_LIST_DOWNLOADS = "SURAH_LIST_DOWNLOADS"
const val VERSES_IDS = "VERSES_IDS"
const val DOWNLOAD_VERSES_IDS = "DOWNLOAD_VERSES_IDS"
const val VERSES_ID = "VERSES_ID"
const val VERSES_LIST = "VERSES_LIST"
const val READERS_DOWNLOADS = "READERS_DOWNLOADS"
const val READER_KEY = "READER_KEY"
const val SURA_LIST_AUDIOS = "SURA_LIST_AUDIOS"
const val LAST_ID = "LAST_ID"
const val AUDIO_DOWNLOAD = "AUDIO_DOWNLOAD"
const val AYA_CONTENT = "AYA_CONTENT"
const val AYA_TRANSLATE = "AYA_TRANSLATE"
const val AYA_TAFSIRS = "AYA_TAFSIRS"
const val SURAH_INDEX = "SURAH_INDEX"


fun mapJuz(juzs: ArrayList<Juz>, suras: ArrayList<Surah>): ArrayList<JuzSurah> {
    val data: ArrayList<JuzSurah> = ArrayList()
    juzs.forEach { juz ->
        val ids = juz.verseMapping?.keys?.map {
            it.toInt()
        }!!
        data.add(JuzSurah(juz.juzNumber, ids, juz.verseMapping, isDownload = juz.isDownload))
        data.addAll(juz.verseMapping.keys.map { key ->
            suras[key.toInt() - 1].apply {
                fromTo = juz.verseMapping[key]
            }
        }.map {
            JuzSurah(juz.juzNumber, ids, juz.verseMapping, it, it.fromTo, juz.isDownload)
        })
    }



    return data
}