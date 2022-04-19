package com.mabrouk.quran_listing_feature.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.mabrouk.quran_listing_feature.data.api.QuranApi
import com.mabrouk.quran_listing_feature.data.db.QuranDao
import com.mabrouk.quran_listing_feature.domain.models.*
import com.mabrouk.quran_listing_feature.domain.response.JuzResponse
import com.mabrouk.quran_listing_feature.domain.response.SurahResponse
import com.mabrouk.quran_listing_feature.domain.response.VersesResponse
import com.mabrouk.quran_listing_feature.domain.respository.QuranDefaultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.mabrouk.core.network.Result
import com.mabrouk.core.network.executeCall
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
class QuranRepository @Inject constructor(
    @ApplicationContext
    val context: Context,
    val api: QuranApi,
    val dao: QuranDao
) : QuranDefaultRepository {

    override suspend fun requestJuz(): Flow<Result<JuzResponse>> {
        return executeCall(context){ api.getJuzs() }
    }

    override suspend fun requestSurahs(): Flow<Result<SurahResponse>> {
        return executeCall(context){ api.getAllSurah() }
    }

    override suspend fun requestVerses(chapter_id: Int, page: Int): Flow<Result<VersesResponse>> {
        return executeCall(context){ api.getSurahVerses(chapter_id, page) }
    }

    override suspend fun saveJuz(juz: ArrayList<Juz>) {
        dao.saveJuzs(juz)
    }

    override suspend fun saveSurahs(surahs: ArrayList<Surah>) {
        dao.saveSurahs(surahs)
    }

    override suspend fun saveVerses(verses: ArrayList<Verse>) {
        dao.saveVerses(verses)
    }

    override fun getSavedSurah(): Flow<List<Surah>> {
       return dao.getSavedSurah()
    }

    override fun getSavedVerses(chapter_id: Int): Flow<List<Verse>> {
        return dao.getSaveVerses(chapter_id)
    }

    override fun getSavedJuz(): Flow<List<Juz>> {
        return dao.getSavedJuzs()
    }

    override fun getSurahById(id: Int): Flow<Surah> {
        return dao.getSurahById(id)
    }

    override suspend fun updateJuz(juz: Juz) {
        dao.updateJUz(juz)
    }

    override suspend fun updateSura(sura: Surah) {
        dao.updateSurah(sura)
    }

    override suspend fun insertReaders(readers: ArrayList<QuranReader>) {
        dao.insertAllReaders(readers)
    }

    override suspend fun updateReader(readers: QuranReader) {
        dao.updateReader(readers)
    }

    override suspend fun searchBySurah(query: String): Flow<List<Surah>> {
        return dao.searchByAtSurah(query)
    }

}