package com.mabrouk.quran_listing_feature.domain.response

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
data class Meta(
    val current_page: Int,
    val next_page: Int?,
    val prev_page: Int?,
    val total_pages: Int?,
    val total_count: Int?
)