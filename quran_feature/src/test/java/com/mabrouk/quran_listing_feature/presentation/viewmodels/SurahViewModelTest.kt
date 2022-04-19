package com.mabrouk.quran_listing_feature.presentation.viewmodels

import com.google.common.truth.Truth.assertThat
import com.mabrouk.core.utils.CoroutineTestRule
import com.mabrouk.core.utils.DataStorePreferences
import com.mabrouk.quran_listing_feature.domain.usecases.AyaRepositoryUseCases
import com.mabrouk.quran_listing_feature.presentation.states.SurahStates
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SurahViewModelTest{
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    lateinit var viewModel: SurahViewModel
    val dataStore = mockk<DataStorePreferences>()

    @Before
    fun setUp(){
        viewModel = SurahViewModel(AyaRepositoryUseCases(FakeSurahRepository()),dataStore)
    }

    @Test
    fun `getTafsier`()= runBlocking{
       val item =  viewModel.getTafsier(1,1).first()
        assertThat(item.isNotEmpty()).isTrue()
    }

    @Test
    fun `updateSurah() success()`() = runBlocking {
        val surah = TestUtils.surahs.first()
        viewModel.updateSurah(surah)
        assertThat(TestUtils.surahs.filter { surah.id == it.id }.size).isEqualTo(1)
    }

    @Test
    fun `getReader()`() = runBlocking {
        viewModel.getReader {
            assertThat(it).isEqualTo(TestUtils.readers.first())
        }
    }

    @Test
    fun `getAllReaders()`() = runBlocking {
        viewModel.getAllReaders {
            assertThat(it).isNotEmpty()
        }
    }

    @Test
    fun `updateReader()`() = runBlocking {
        viewModel.updateReader(TestUtils.readers.first())
        assertThat(viewModel.states.value is SurahStates.UpdateReader).isTrue()
    }

}