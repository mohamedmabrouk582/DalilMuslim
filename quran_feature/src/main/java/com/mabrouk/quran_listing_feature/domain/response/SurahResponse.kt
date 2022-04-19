package com.mabrouk.quran_listing_feature.domain.response

import com.mabrouk.quran_listing_feature.domain.models.Surah

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
data class SurahResponse(
    val chapters:ArrayList<Surah>
)
