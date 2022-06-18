package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Word(
    val id:Long,
    val position:Int,
    @SerializedName("text_indopak")
    val textIndopak:String,
    @SerializedName("verse_key")
    val verseKey:String,
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
        @SerializedName("language_name")
        val languageName:String
    ) : Parcelable
}