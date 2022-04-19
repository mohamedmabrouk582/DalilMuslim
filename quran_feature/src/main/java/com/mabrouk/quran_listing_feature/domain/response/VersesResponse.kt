package com.mabrouk.quran_listing_feature.domain.response

import com.google.gson.annotations.SerializedName
import com.mabrouk.quran_listing_feature.domain.models.Verse

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
data class VersesResponse(
    val verses:ArrayList<Verse>,
    @SerializedName("pagination")
    val meta: Meta
)
