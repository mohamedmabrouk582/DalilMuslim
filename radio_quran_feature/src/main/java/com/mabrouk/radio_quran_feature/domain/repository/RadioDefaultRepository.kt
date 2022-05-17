package com.mabrouk.radio_quran_feature.domain.repository

import com.mabrouk.core.network.Result
import com.mabrouk.radio_quran_feature.domain.models.Radio
import com.mabrouk.radio_quran_feature.domain.models.RadioResponse
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
interface RadioDefaultRepository {
    fun requestRadios(): Flow<Result<RadioResponse>>
    suspend fun getSavedRadios() : ArrayList<Radio>
    suspend fun saveRadios(data : ArrayList<Radio>)
}