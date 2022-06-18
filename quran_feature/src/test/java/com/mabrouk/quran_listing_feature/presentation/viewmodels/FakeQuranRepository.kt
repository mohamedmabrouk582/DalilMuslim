package com.mabrouk.quran_listing_feature.presentation.viewmodels

import com.mabrouk.core.network.Result
import com.mabrouk.quran_listing_feature.domain.models.Juz
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.Verse
import com.mabrouk.quran_listing_feature.domain.response.JuzResponse
import com.mabrouk.quran_listing_feature.domain.response.Meta
import com.mabrouk.quran_listing_feature.domain.response.SurahResponse
import com.mabrouk.quran_listing_feature.domain.response.VersesResponse
import com.mabrouk.quran_listing_feature.domain.respository.QuranDefaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
class FakeQuranRepository : QuranDefaultRepository {

    override  fun requestJuz(): Flow<Result<JuzResponse>> {
        return flow {
            emit(Result.OnSuccess(JuzResponse(TestUtils.juzs)))
        }
    }

    override  fun requestSurahs(): Flow<Result<SurahResponse>> {
        return flow {
            emit(Result.OnSuccess(SurahResponse(TestUtils.surahs)))
        }
    }

    override  fun requestVerses(chapter_id: Int, page: Int): Flow<Result<VersesResponse>> {
        return flow {
            emit(
                Result.OnSuccess(
                    VersesResponse(
                        TestUtils.verses,
                        Meta(1, 1, 1, 1, TestUtils.verses.size)
                    )
                )
            )
        }
    }

    override suspend fun saveJuz(juz: ArrayList<Juz>) {
        TestUtils.savedJuz= juz
    }

    override suspend fun saveSurahs(surahs: ArrayList<Surah>) {
        TestUtils.savedSurahs.addAll(surahs)
    }

    override suspend fun saveVerses(verses: ArrayList<Verse>) {
        TestUtils.savedVerse.addAll(verses)
    }

    override fun getSavedSurah(): Flow<List<Surah>> {
        return flow {
            emit(TestUtils.savedSurahs)
        }
    }

    override fun getSavedVerses(chapter_id: Int): Flow<List<Verse>> {
        return flow {
            emit(TestUtils.verses.filter { it.chapterId == chapter_id })
        }
    }

    override fun getSavedJuz(): Flow<List<Juz>> {
        return flow {
            emit(TestUtils.savedJuz)
        }
    }

    override fun getSurahById(id: Int): Flow<Surah> {
        return flow {
            emit(TestUtils.savedSurahs.find { it.id == id }!!)
        }
    }

    override suspend fun updateJuz(juz: Juz) {
        TestUtils.juzs.find { it.id == juz.id }?.apply {
            TestUtils.juzs.remove(this)
        }
        TestUtils.juzs.add(juz)
    }

    override suspend fun updateSura(sura: Surah) {
        TestUtils.surahs.find { it.id == sura.id }?.apply {
            TestUtils.surahs.remove(this)
        }
        TestUtils.surahs.add(sura)
    }

    override suspend fun insertReaders(readers: ArrayList<QuranReader>) {
        TestUtils.readers = readers
    }

    override suspend fun updateReader(readers: QuranReader) {
        TestUtils.readers.find { it.readerId == readers.readerId }?.apply {
            TestUtils.readers.remove(this)
        }
        TestUtils.readers.add(readers)
    }

    override  fun searchBySurah(query: String): Flow<List<Surah>> {
        return flow {
            emit(arrayListOf())
        }
    }
}