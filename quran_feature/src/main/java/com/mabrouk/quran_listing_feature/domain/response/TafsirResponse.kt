package com.mabrouk.quran_listing_feature.domain.response

import com.mabrouk.quran_listing_feature.domain.models.TafsirAya

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
data class TafsirResponse(
    val tafsirs:ArrayList<TafsirAya>
)
