package com.mabrouk.quran_listing_feature.domain.respository

import androidx.lifecycle.LiveData
import com.mabrouk.core.network.Result
import com.mabrouk.quran_listing_feature.domain.models.*
import com.mabrouk.quran_listing_feature.domain.response.JuzResponse
import com.mabrouk.quran_listing_feature.domain.response.SurahResponse
import com.mabrouk.quran_listing_feature.domain.response.VersesResponse
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
interface QuranDefaultRepository {
    suspend fun requestJuz() : Flow<Result<JuzResponse>>
    suspend fun requestSurahs() : Flow<Result<SurahResponse>>
    suspend fun requestVerses(chapter_id:Int,page:Int=0) : Flow<Result<VersesResponse>>
    suspend fun saveJuz(juz:ArrayList<Juz>)
    suspend fun saveSurahs(surahs : ArrayList<Surah>)
    suspend fun saveVerses(verses : ArrayList<Verse>)
    fun getSavedSurah() : Flow<List<Surah>>
    fun getSavedVerses(chapter_id:Int) : Flow<List<Verse>>
    fun getSavedJuz() : Flow<List<Juz>>
    fun getSurahById(id:Int) : Flow<Surah>
    suspend fun updateJuz(juz: Juz)
    suspend fun updateSura(sura: Surah)
    suspend fun insertReaders(readers:ArrayList<QuranReader>)
    suspend fun updateReader(readers:QuranReader)
    suspend fun searchBySurah(query:String) : Flow<List<Surah>>
}