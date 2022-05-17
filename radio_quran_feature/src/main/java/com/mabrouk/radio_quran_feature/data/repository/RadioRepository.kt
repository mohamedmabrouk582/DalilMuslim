package com.mabrouk.radio_quran_feature.data.repository

import android.content.Context
import com.mabrouk.core.network.Result
import com.mabrouk.core.network.executeCall
import com.mabrouk.core.network.toArrayList
import com.mabrouk.radio_quran_feature.data.api.RadioApi
import com.mabrouk.radio_quran_feature.data.db.RadioDao
import com.mabrouk.radio_quran_feature.domain.models.Radio
import com.mabrouk.radio_quran_feature.domain.models.RadioResponse
import com.mabrouk.radio_quran_feature.domain.repository.RadioDefaultRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
class RadioRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val api: RadioApi ,
    val dao: RadioDao
) : RadioDefaultRepository {
    override fun requestRadios(): Flow<Result<RadioResponse>> {
        return executeCall(context){api.getRadios("http://api.mp3quran.net/radios/radio_arabic.json")}
    }

    override suspend fun getSavedRadios(): ArrayList<Radio> {
      return dao.getRadios().first().toArrayList()
    }

    override suspend fun saveRadios(data: ArrayList<Radio>) {
        dao.insertAllRadios(data)
    }
}