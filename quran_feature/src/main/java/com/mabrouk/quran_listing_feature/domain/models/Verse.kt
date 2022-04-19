package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.*
import com.mabrouk.core.network.toArrayList
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@Entity
@Parcelize
data class Verse(
    @PrimaryKey
    val id: Int,
    val verse_number: Int,
    val chapter_id: Int,
    val text_madani: String? = null,
    val text_indopak: String? = null,
    val text_simple: String? = null,
    val juz_number: Int? = null,
    val hizb_number: Int? = null,
    val rub_number: Int? = null,
    val sajdah: String? = null,
    val sajdah_number: Int? = null,
    val page_number: Int? = null,
    @Embedded
    val audio: Audio? = null,
    val translations: ArrayList<Translation>? = null,
    val media_contents: ArrayList<Media>? = null,
    val words: ArrayList<Word>? = null,
    var selected: Boolean = false
) : Parcelable {
    @Ignore
    fun getTranslation(): ArrayList<Translation>? {
        return words?.let {
            it.removeLast()
            it
        }?.map {
            Translation(
                it.id,
                it.translation.language_name,
                it.translation.text,
                it.text_indopak,
                it.verse_key,
                it.audio.url,
                it.transliteration.text
            )
        }?.toArrayList()
    }
}
