package com.mabrouk.radio_quran_feature.domain.usecases

import com.mabrouk.core.network.Result
import com.mabrouk.radio_quran_feature.domain.models.Radio
import com.mabrouk.radio_quran_feature.domain.models.RadioResponse
import com.mabrouk.radio_quran_feature.domain.repository.RadioDefaultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
class RadioUseCase @Inject constructor(val repository: RadioDefaultRepository) {

    fun requestRadios(): Flow<Result<RadioResponse>>{
        return repository.requestRadios()
    }

    suspend fun getSavedRadios() : ArrayList<Radio>{
        return repository.getSavedRadios()
    }


    suspend fun saveRadios(data: ArrayList<Radio>) {
        repository.saveRadios(data)
    }
}