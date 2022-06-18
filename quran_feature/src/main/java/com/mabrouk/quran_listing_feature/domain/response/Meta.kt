package com.mabrouk.quran_listing_feature.domain.response

import com.google.gson.annotations.SerializedName

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
data class Meta(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("next_page")
    val nextPage: Int?,
    @SerializedName("prev_page")
    val prevPage: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_count")
    val totalCount: Int?
)