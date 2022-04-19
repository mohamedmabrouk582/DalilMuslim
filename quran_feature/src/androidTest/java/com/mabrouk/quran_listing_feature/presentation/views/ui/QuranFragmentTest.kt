package com.mabrouk.quran_listing_feature.presentation.views.ui

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.mabrouk.core.utils.CoroutineTestRule
import com.mabrouk.quran_listing_feature.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import launchFragmentInHiltContainer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class QuranFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())


    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun isQuranClick() {
        launchQuranListFragment()
        onView(withId(R.id.quranRcv)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1,click()))
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.surahFragment)
    }


    @Test
    fun launchQuranListFragment(){
        launchFragmentInHiltContainer<QuranFragment>{
            navController.setGraph(R.id.quran_graph)
            navController.setCurrentDestination(R.id.quranFragment)
            this.viewLifecycleOwnerLiveData.observeForever {
                Navigation.setViewNavController(this.requireView(),navController)
            }
        }
    }

}