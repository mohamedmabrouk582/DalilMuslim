package com.mabrouk.quran_listing_feature

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.google.firebase.FirebaseApp
import dagger.hilt.android.testing.HiltTestApplication

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/18/22
 */
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
       // FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

}