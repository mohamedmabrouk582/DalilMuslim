package com.mabrouk.quran_listing_feature.presentation.viewmodels

import com.mabrouk.core.network.Result
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.TafsirAya
import com.mabrouk.quran_listing_feature.domain.respository.AyaDefaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
class FakeSurahRepository : AyaDefaultRepository {
    override  fun requestTafsir(
        chapter_id: Int,
        verse_id: Int,
        id: Int
    ): Flow<Result<TafsirAya>> {
        return flow {
            emit(Result.OnSuccess(TestUtils.tafsirAya))
        }
    }

    override suspend fun saveTafsir(data: TafsirAya) {
        TestUtils.tafsirAya = data
    }

    override fun getSavedTafsir(chapter_id: Int, verse_id: Int): Flow<List<TafsirAya>> {
        return flow {
            emit(arrayListOf(TestUtils.tafsirAya))
        }
    }

    override  fun downloadAudio(url: String): Flow<Result<ResponseBody>> {
        return flow {  }
    }

    override suspend fun updateSurah(surah: Surah) {
        TestUtils.surahs.find { it.id == surah.id }?.apply {
            TestUtils.surahs.remove(this)
        }
        TestUtils.surahs.add(surah)
    }

    override fun getReader(id: Int): Flow<QuranReader> {
        return flow {
            emit(TestUtils.readers.find { it.readerId == id }!!)
        }
    }

    override suspend fun getReaders(): ArrayList<QuranReader> {
        return TestUtils.readers
    }

    override suspend fun updateQuranReader(item: QuranReader) {
        TestUtils.readers.find { it.readerId == item.readerId }?.apply {
            TestUtils.readers.remove(this)
        }
        TestUtils.readers.add(item)
    }
}