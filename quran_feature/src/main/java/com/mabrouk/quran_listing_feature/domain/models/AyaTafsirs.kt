package com.mabrouk.quran_listing_feature.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
@Parcelize
data class AyaTafsirs(val name:String,val tafsirs:ArrayList<TafsirAya>): Parcelable