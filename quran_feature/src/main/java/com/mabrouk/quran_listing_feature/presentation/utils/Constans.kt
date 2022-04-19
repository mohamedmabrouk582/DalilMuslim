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


fun mapJuz(juzs: ArrayList<Juz>, suras: ArrayList<Surah>): ArrayList<JuzSurah> {
    val data: ArrayList<JuzSurah> = ArrayList()
    juzs.forEach { juz ->
        val ids = juz.verse_mapping?.keys?.map {
            it.toInt()
        }!!
        data.add(JuzSurah(juz.juz_number, ids, juz.verse_mapping, isDownload = juz.isDownload))
        data.addAll(juz.verse_mapping.keys.map { key ->
            suras[key.toInt() - 1].apply {
                from_to = juz.verse_mapping[key]
            }
        }.map {
            JuzSurah(juz.juz_number, ids, juz.verse_mapping, it, it.from_to, juz.isDownload)
        })
    }

    data.forEach {
        Log.d("susususu", "${it}")
    }

    return data
}