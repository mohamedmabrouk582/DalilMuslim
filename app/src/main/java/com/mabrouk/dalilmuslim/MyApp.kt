package com.mabrouk.dalilmuslim

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.google.firebase.FirebaseApp
import com.mabrouk.core.utils.DataStorePreferences
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class MyApp : LocalizationApplication()  , Configuration.Provider{
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var networkFlipperPlugin: NetworkFlipperPlugin

    override fun getDefaultLanguage(base: Context): Locale {
        return Locale("ar")
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            SoLoader.init(this, false)

            if (FlipperUtils.shouldEnableFlipper(this)) {
                AndroidFlipperClient.getInstance(this).apply {
                    addPlugin(InspectorFlipperPlugin(this@MyApp, DescriptorMapping.withDefaults()))
                    addPlugin(CrashReporterPlugin.getInstance())
                    addPlugin(DatabasesFlipperPlugin(this@MyApp))
                    addPlugin(networkFlipperPlugin)
                    addPlugin(SharedPreferencesFlipperPlugin(this@MyApp, DataStorePreferences.FILE_NAME))
                }.start()
            }
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}