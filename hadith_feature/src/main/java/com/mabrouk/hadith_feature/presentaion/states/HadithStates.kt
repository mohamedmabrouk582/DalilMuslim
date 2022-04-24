package com.mabrouk.hadith_feature.presentaion.states

import com.mabrouk.hadith_feature.domain.models.HadithBookNumber
import com.mabrouk.hadith_feature.domain.models.HadithCategory
import com.mabrouk.hadith_feature.domain.models.HadithModel

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
sealed class HadithStates {
    object Idle : HadithStates()
    data class LoadCategories(val data: ArrayList<HadithCategory>) : HadithStates()
    data class LoadHadithBooks(val data: ArrayList<HadithBookNumber>) : HadithStates()
    data class LoadHadith(val data: ArrayList<HadithModel>) : HadithStates()
}
