package com.mabrouk.azkar_feature.domain.usecases

import com.mabrouk.azkar_feature.domain.models.Azkar
import com.mabrouk.azkar_feature.domain.repository.AzkarDefaultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
class AzkarUseCase @Inject constructor(val repository: AzkarDefaultRepository) {

     fun getAzkarBYCategory(category: String): Flow<List<Azkar>> {
        return repository.getAzkarBYCategory(category)
    }

     fun getAzkarCategories(): Flow<List<String>> {
        return repository.getAzkarCategories()
    }
}