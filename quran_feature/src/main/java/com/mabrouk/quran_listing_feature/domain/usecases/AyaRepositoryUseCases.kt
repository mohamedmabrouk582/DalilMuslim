package com.mabrouk.quran_listing_feature.domain.usecases

import com.mabrouk.core.network.Result
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.TafsirAya
import com.mabrouk.quran_listing_feature.domain.respository.AyaDefaultRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
class AyaRepositoryUseCases @Inject constructor(val repository: AyaDefaultRepository) {

    suspend fun requestTafsir(chapter_id:Int , verse_id:Int,id:Int) : Flow<Result<TafsirAya>>{
      return repository.requestTafsir(chapter_id, verse_id, id)
    }
    suspend fun saveTafsir(data: TafsirAya){
        repository.saveTafsir(data)
    }
    fun getSavedTafsir(chapter_id: Int,verse_id: Int) : Flow<List<TafsirAya>>{
       return repository.getSavedTafsir(chapter_id, verse_id)
    }
    suspend fun downloadAudio(url:String) : Flow<Result<ResponseBody>>{
        return repository.downloadAudio(url)
    }
    suspend fun updateSurah(surah: Surah){
       repository.updateSurah(surah)
    }
    fun getReader(id:Int): Flow<QuranReader>{
        return repository.getReader(id)
    }
    suspend fun getReaders() : ArrayList<QuranReader>{
       return repository.getReaders()
    }
    suspend fun updateQuranReader(item: QuranReader){
        repository.updateQuranReader(item)
    }

}