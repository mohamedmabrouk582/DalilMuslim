package com.mabrouk.quran_listing_feature.presentation.viewmodels

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mabrouk.core.utils.CoroutineTestRule
import com.mabrouk.core.utils.DataStorePreferences
import com.mabrouk.quran_listing_feature.domain.usecases.QuranRepositoryUseCase
import com.mabrouk.quran_listing_feature.presentation.states.QuranStates
import com.mabrouk.quran_listing_feature.presentation.utils.READERS_DOWNLOADS
import com.mabrouk.quran_listing_feature.presentation.utils.SURAH_LIST_DOWNLOADS
import com.mabrouk.quran_listing_feature.presentation.utils.mapJuz
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */

@ExperimentalCoroutinesApi
class QuranViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    lateinit var viewModel: QuranViewModel
    val dataStore = mockk<DataStorePreferences>()
    val remoteConfig = mockk<FirebaseRemoteConfig>()

    @Before
    fun setUp() {
        viewModel = QuranViewModel(QuranRepositoryUseCase(FakeQuranRepository()), remoteConfig, dataStore)
    }


    @Test
    fun `requestJuz() success`() {
        viewModel.requestJuz()
        assertThat(viewModel.juzs.isNotEmpty()).isTrue()
    }

    @Test
    fun `requestSurahs() success`() {
        viewModel.requestSurahs()
        assertThat(viewModel.surahs.isNotEmpty()).isTrue()
    }

    @Test
    fun `requestVerses() return Value `() = runBlocking {
        val value = viewModel.fetchVerse(1).first()
        assertThat(value.isNotEmpty()).isTrue()
    }

    @Test
    fun `saveJuz() success`() {
        viewModel.requestJuz()
        assertThat(TestUtils.savedJuz.size).isEqualTo(TestUtils.juzs.size)
    }

    @Test
    fun `saveSurahs() success`() {
        viewModel.requestSurahs()
        assertThat(TestUtils.savedSurahs.size).isEqualTo(TestUtils.surahs.size)
    }

    @Test
    fun `fetchVerse() `() = runBlocking {
        val item = viewModel.fetchVerse(1).first()
        assertThat(item.isNotEmpty()).isTrue()
    }

}