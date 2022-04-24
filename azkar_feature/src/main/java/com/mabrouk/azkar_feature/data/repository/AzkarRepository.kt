package com.mabrouk.azkar_feature.data.repository

import com.mabrouk.azkar_feature.data.db.AzkarDao
import com.mabrouk.azkar_feature.domain.models.Azkar
import com.mabrouk.azkar_feature.domain.repository.AzkarDefaultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
class AzkarRepository @Inject constructor(val dao: AzkarDao) : AzkarDefaultRepository {

    override fun getAzkarBYCategory(category: String): Flow<List<Azkar>> {
        return dao.getAzkarBYCategory(category)
    }

    override fun getAzkarCategories(): Flow<List<String>> {
        return dao.getAzkarCategories()
    }
}