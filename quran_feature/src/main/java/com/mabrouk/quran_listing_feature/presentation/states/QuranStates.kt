package com.mabrouk.quran_listing_feature.presentation.states

import com.mabrouk.quran_listing_feature.domain.models.JuzSurah
import com.mabrouk.quran_listing_feature.domain.models.SurahFts

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
sealed class QuranStates{
    object IDLE : QuranStates()
    data class LoadJuzSurahs(val juzSurah: ArrayList<JuzSurah>) : QuranStates()
    data class Error(val error:String) : QuranStates()
    data class SearchResult(val juzSurah: ArrayList<JuzSurah>) : QuranStates()
    object ClearSearch : QuranStates()
}
