package com.mabrouk.quran_listing_feature.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.mabrouk.core.utils.CoroutineTestRule
import com.mabrouk.quran_listing_feature.data.api.QuranApi
import com.mabrouk.quran_listing_feature.data.api.TafseerApi
import com.mabrouk.quran_listing_feature.domain.response.JuzResponse
import com.mabrouk.quran_listing_feature.domain.response.SurahResponse
import com.mabrouk.quran_listing_feature.domain.response.VersesResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@HiltAndroidTest
@SmallTest
@ExperimentalCoroutinesApi
class TestQuranApi {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    lateinit var mockWebServer: MockWebServer

    @Inject
    @Named("test_api")
    lateinit var quranApi: QuranApi

    @Inject
    @Named("test_tafseer_api")
    lateinit var tafseerApi: TafseerApi

    @Before
    fun setUp(){
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun requestJuzs() = runBlocking {
        val juzs = quranApi.getJuzs().body()
        val items = MockServerDispatcher().getJsonContent("juzs.json")
        val data = Gson().fromJson(items, JuzResponse::class.java)
        assertThat(data).isEqualTo(juzs)
    }

    @Test
    fun requestSurahs() = runBlocking {
        val surahs = quranApi.getAllSurah().body()
        val items = MockServerDispatcher().getJsonContent("surahs.json")
        val data = Gson().fromJson(items,SurahResponse::class.java)
        assertThat(data).isEqualTo(surahs)
    }

    @Test
    fun getSurahVerses() = runBlocking {
        val verses = quranApi.getSurahVerses(1,1).body()
        val items = MockServerDispatcher().getJsonContent("verse1.json")
        val data = Gson().fromJson(items,VersesResponse::class.java)
        assertThat(data).isEqualTo(verses)
    }

    @Test
    fun getAyaTafseer(): Unit = runBlocking {
        try {
            tafseerApi.getAyaTafseer(1,1,1).await()
        }catch (e : HttpException){
            assertThat(e.code()).isEqualTo(400)
        }
    }




}