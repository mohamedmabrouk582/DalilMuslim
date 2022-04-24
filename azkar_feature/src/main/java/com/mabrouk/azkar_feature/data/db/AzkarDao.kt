package com.mabrouk.azkar_feature.data.db

import androidx.room.Dao
import androidx.room.Query
import com.mabrouk.azkar_feature.domain.models.Azkar
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@Dao
interface AzkarDao {
    @Query("select * from azkar where category =:category")
    fun getAzkarBYCategory(category:String) : Flow<List<Azkar>>

    @Query("select category from azkar")
    fun getAzkarCategories() : Flow<List<String>>
}