package com.mabrouk.hadith_feature.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@Parcelize
data class PassHadithKeys(
    val name: String,
    val bookNumber: String
) : Parcelable
