package com.mabrouk.dalilmuslim

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class MyApp : LocalizationApplication()  , Configuration.Provider{
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getDefaultLanguage(base: Context): Locale {
        return Locale("ar")
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}