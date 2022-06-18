package com.mabrouk.quran_listing_feature.domain.usecases

import com.mabrouk.core.network.Result
import com.mabrouk.quran_listing_feature.domain.models.Juz
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.Verse
import com.mabrouk.quran_listing_feature.domain.response.JuzResponse
import com.mabrouk.quran_listing_feature.domain.response.SurahResponse
import com.mabrouk.quran_listing_feature.domain.response.VersesResponse
import com.mabrouk.quran_listing_feature.domain.respository.QuranDefaultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
class QuranRepositoryUseCase @Inject constructor(val repository: QuranDefaultRepository) {
     fun requestJuz() : Flow<Result<JuzResponse>>{
        return repository.requestJuz()
    }
     fun requestSurahs() : Flow<Result<SurahResponse>>{
        return repository.requestSurahs()
    }
     fun requestVerses(chapterId:Int,page:Int=0) : Flow<Result<VersesResponse>>{
        return repository.requestVerses(chapterId, page)
    }
    suspend fun saveJuz(juz:ArrayList<Juz>){
        repository.saveJuz(juz)
    }
    suspend fun saveSurahs(surahs : ArrayList<Surah>){
        repository.saveSurahs(surahs)
    }
    suspend fun saveVerses(verses : ArrayList<Verse>){
        repository.saveVerses(verses)
    }
    fun getSavedSurah() : Flow<List<Surah>>{
       return repository.getSavedSurah()
    }
    fun getSavedVerses(chapterId:Int) : Flow<List<Verse>>{
        return repository.getSavedVerses(chapterId)
    }
    fun getSavedJuz() : Flow<List<Juz>>{
        return repository.getSavedJuz()
    }
    fun getSurahById(id:Int) : Flow<Surah>{
        return repository.getSurahById(id)
    }
    suspend fun updateJuz(juz: Juz){
        repository.updateJuz(juz)
    }
    suspend fun updateSura(sura: Surah){
        repository.updateSura(sura)
    }
    suspend fun insertReaders(readers:ArrayList<QuranReader>){
        repository.insertReaders(readers)
    }
    suspend fun updateReader(readers: QuranReader){
        repository.updateReader(readers)
    }

     fun searchBySurah(query:String) : Flow<List<Surah>>{
        return repository.searchBySurah(query)
    }
}