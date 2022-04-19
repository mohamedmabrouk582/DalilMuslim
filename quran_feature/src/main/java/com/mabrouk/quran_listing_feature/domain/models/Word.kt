package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Word(
    val id:Long,
    val position:Int,
    val text_indopak:String,
    val verse_key:String,
    val transliteration: Transliteration,
    val audio: Audio,
    val translation: Translation
) : Parcelable {

    @Parcelize
    data class Transliteration(
        val text:String
    ) : Parcelable

    @Parcelize
    data class Audio(
        val url:String
    ) : Parcelable

    @Parcelize
    data class Translation(
        val text: String,
        val language_name:String
    ) : Parcelable
}