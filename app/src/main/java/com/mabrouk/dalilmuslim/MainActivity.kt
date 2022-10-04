package com.mabrouk.dalilmuslim

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.mabrouk.core.network.loadLibrary
import com.mabrouk.dalilmuslim.databinding.ActivityMainBinding
import com.mabrouk.history_feature.peresntaion.view.StoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : LocalizationActivity() {
    lateinit var viewBinding: ActivityMainBinding
    lateinit var navController: NavController
    val isPipeSupported by lazy {
        packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        loadLibrary()
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav)


        navHostFragment?.findNavController()?.also { navController = it }
        viewBinding.navView.setupWithNavController(navController)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewBinding.navView.visibility = if (isInPictureInPictureMode) View.GONE else View.VISIBLE

    }

    private fun updatePipPrams(): PictureInPictureParams? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PictureInPictureParams.Builder()
                .setSourceRectHint(StoryFragment.sourceRectHint)
                .setAspectRatio(Rational(20, 14))
                .setAutoEnterEnabled(true)
                .build()
        } else null
    }

    override fun onUserLeaveHint() {
        if (isPipeSupported && (navController.currentDestination?.label ?: "") in arrayOf(
                "QuranRadioFragment", "StoryFragment"
            )
        ) {
            updatePipPrams()?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewBinding.navView.visibility = View.GONE
                    enterPictureInPictureMode(it)
                }
            }
        }
        super.onUserLeaveHint()
    }
}