package com.mabrouk.quran_listing_feature.data.repository

import android.content.Context
import com.mabrouk.core.network.Result
import com.mabrouk.core.network.executeCall
import com.mabrouk.core.network.executeCall2
import com.mabrouk.core.network.toArrayList
import com.mabrouk.quran_listing_feature.data.api.QuranApi
import com.mabrouk.quran_listing_feature.data.api.TafseerApi
import com.mabrouk.quran_listing_feature.data.db.QuranDao
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.TafsirAya
import com.mabrouk.quran_listing_feature.domain.respository.AyaDefaultRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
class AyaRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val quranApi: QuranApi,
    private val api: TafseerApi,
    private val dao: QuranDao
)  : AyaDefaultRepository{


    override  fun requestTafsir(
        chapterId: Int,
        verseId: Int,
        id: Int
    ): Flow<Result<TafsirAya>> {
        return executeCall2(context = context) { api.getAyaTafseer(id,chapterId,verseId) }
    }

    override suspend fun saveTafsir(data: TafsirAya) {
        dao.saveVerseTafsir(data)
    }

    override fun getSavedTafsir(chapterId: Int, verseId: Int): Flow<List<TafsirAya>> {
        return dao.getSavedTafsir("/quran/$chapterId/$verseId/")
    }

    override  fun downloadAudio(url: String): Flow<Result<ResponseBody>> {
        return executeCall(context){
            quranApi.downloadAudio(url)
        }
    }

    override suspend fun updateSurah(surah: Surah) {
        dao.updateSurah(surah)
    }

    override fun getReader(id: Int): Flow<QuranReader> {
        return dao.getReader(id)
    }

    override suspend fun getReaders(): ArrayList<QuranReader> {
        return dao.getAllReaders().first().toArrayList()
    }

    override suspend fun updateQuranReader(item: QuranReader) {
        return dao.updateReader(item)
    }

}