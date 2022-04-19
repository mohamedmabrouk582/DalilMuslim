package com.mabrouk.quran_listing_feature.domain.respository

import com.mabrouk.core.network.Result
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.TafsirAya
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
interface AyaDefaultRepository {
    suspend fun requestTafsir(chapter_id:Int , verse_id:Int,id:Int) : Flow<Result<TafsirAya>>
    suspend fun saveTafsir(data:TafsirAya)
    fun getSavedTafsir(chapter_id: Int,verse_id: Int) : Flow<List<TafsirAya>>
    suspend fun downloadAudio(url:String) : Flow<Result<ResponseBody>>
    suspend fun updateSurah(surah: Surah)
    fun getReader(id:Int):Flow<QuranReader>
    suspend fun getReaders() : ArrayList<QuranReader>
    suspend fun updateQuranReader(item:QuranReader)
}