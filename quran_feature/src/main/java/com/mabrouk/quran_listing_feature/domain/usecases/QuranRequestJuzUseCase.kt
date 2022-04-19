package com.mabrouk.quran_listing_feature.domain.usecases

import com.mabrouk.core.network.Result
import com.mabrouk.quran_listing_feature.domain.response.JuzResponse
import com.mabrouk.quran_listing_feature.domain.respository.QuranDefaultRepository
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
class QuranRequestJuzUseCase constructor(val repository: QuranDefaultRepository){
    suspend  operator fun invoke(): Flow<Result<JuzResponse>> {
        return repository.requestJuz()
    }
}