package com.mabrouk.azkar_feature.domain.repository

import com.mabrouk.azkar_feature.domain.models.Azkar
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
interface AzkarDefaultRepository {
    fun getAzkarBYCategory(category:String) : Flow<List<Azkar>>
    fun getAzkarCategories() : Flow<List<String>>
}