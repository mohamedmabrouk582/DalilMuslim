package com.mabrouk.quran_listing_feature.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.mabrouk.core.network.toArrayList
import com.mabrouk.core.utils.CoroutineTestRule
import com.mabrouk.quran_listing_feature.TestUtils
import com.mabrouk.quran_listing_feature.data.db.QuranDB
import com.mabrouk.quran_listing_feature.data.db.QuranDao
import com.mabrouk.quran_listing_feature.domain.models.Juz
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.Verse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/18/22
 */
@HiltAndroidTest
@SmallTest
@ExperimentalCoroutinesApi
class QuranDbTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Inject
    @Named("test_db")
    lateinit var db : QuranDB
    lateinit var dao : QuranDao

    @Before
    fun setup(){
        hiltRule.inject()
        dao = db.getQuranDao()
    }

    @After
    fun tearDown(){
        db.close()
    }

    @Test
    fun insertAllReaders() = runBlocking {
        dao.insertAllReaders(TestUtils.readers)
        val items = dao.getAllReaders().first()
        assertThat(items.isNotEmpty()).isTrue()
    }

    @Test
    fun saveSurahs() = runBlocking {
        dao.saveSurahs(TestUtils.surahs)
        assertThat(dao.getSavedSurah().first()).isEqualTo(TestUtils.surahs)
    }

    @Test
    fun saveJuz() = runBlocking {
        dao.saveJuzs(TestUtils.juz)
        assertThat(dao.getSavedJuzs().first()).isEqualTo(TestUtils.juz)
    }

    @Test
    fun saveVerses() = runBlocking {
        dao.saveVerses(TestUtils.verses)
        assertThat(dao.getSaveVerses(1).first()).isEqualTo(TestUtils.verses.filter { it.chapter_id == 1 }.toArrayList())
    }

    @Test
    fun saveVerseTafsir() = runBlocking {
        dao.saveVerseTafsir(TestUtils.tafsirAya)
        val items = dao.getSavedTafsir("/quran/1/1/").first()
        assertThat(items.toArrayList()).isEqualTo(arrayListOf(TestUtils.tafsirAya))
    }

    @Test
    fun updateJUz() = runBlocking {
        dao.saveJuzs(TestUtils.juzs)
        val juz = Juz(1,2)
        dao.updateJUz(juz)
        val item = dao.getSavedJuzs().first().find { it.id == juz.id }
        assertThat(item).isEqualTo(juz)
    }

    @Test
    fun updateVerse() = runBlocking {
        dao.saveVerses(TestUtils.verses)
        val verse = Verse(1,1,1,"test")
        dao.updateVerse(verse)
        val item = dao.getSaveVerses(1).first()
        assertThat(verse).isIn(item)
    }

    @Test
    fun updateReader() = runBlocking {
        dao.insertAllReaders(TestUtils.readers)
        val reader = QuranReader(1,"reader 1","", versesIds = arrayListOf(1,2,3))
        dao.updateReader(reader)
        val items = dao.getAllReaders().first()
        assertThat(reader).isIn(items)

    }

    @Test
    fun updateSurah() = runBlocking {
        saveSurahs()
        val surah = Surah(1, revelation_place = "text")
        dao.updateSurah(surah)
        val items = dao.getSavedSurah().first()
        assertThat(surah).isIn(items)
    }


}