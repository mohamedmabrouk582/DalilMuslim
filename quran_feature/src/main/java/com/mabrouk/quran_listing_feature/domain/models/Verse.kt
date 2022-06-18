package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
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
    @SerializedName("verse_number")
    val verseNumber: Int,
    @SerializedName("chapter_id")
    val chapterId: Int,
    @SerializedName("text_madani")
    val textMadani: String? = null,
    @SerializedName("text_indopak")
    val textIndopak: String? = null,
    @SerializedName("text_simple")
    val textSimple: String? = null,
    @SerializedName("juz_number")
    val juzNumber: Int? = null,
    @SerializedName("hizb_number")
    val hizbNumber: Int? = null,
    @SerializedName("rub_number")
    val rubNumber: Int? = null,
    val sajdah: String? = null,
    @SerializedName("sajdah_number")
    val sajdahNumber: Int? = null,
    @SerializedName("page_number")
    val pageNumber: Int? = null,
    @Embedded
    val audio: Audio? = null,
    val translations: ArrayList<Translation>? = null,
    @SerializedName("media_contents")
    val mediaContents: ArrayList<Media>? = null,
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
                it.translation.languageName,
                it.translation.text,
                it.textIndopak,
                it.verseKey,
                it.audio.url,
                it.transliteration.text
            )
        }?.toArrayList()
    }
}
