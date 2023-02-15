package com.mabrouk.dalilmuslim

import android.Manifest
import android.app.AlarmManager
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Rational
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.mabrouk.prayertime.presentaion.viewmodels.PrayerViewModel
import com.mabrouk.core.utils.*
import com.mabrouk.dalilmuslim.databinding.ActivityMainBinding
import com.mabrouk.history_feature.peresntaion.view.StoryFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : LocalizationActivity() {
    lateinit var viewBinding: ActivityMainBinding
    lateinit var navController: NavController
    val viewModel: PrayerViewModel by viewModels()

    private val isPipeSupported by lazy {
        packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }
    private val locationClient by lazy {
        DefaultLocationClient(applicationContext)
    }
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private var activityForResultLauncher: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivity()
        checkPermission()
        checkAlarmPermission()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav)

        navHostFragment?.findNavController()?.also { navController = it }
        viewBinding.navView.setupWithNavController(navController)
        getLocation()
    }


    private fun getLocation() {
        locationClient.getMyCurrentLocation(lifecycleScope, locationError = {
            if (it == LocationError.GPS) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activityForResultLauncher?.launch(intent)
            }
        }) {
            viewModel.startService(applicationContext, it.latitude, it.longitude)
        }
    }

    private fun registerActivity() {
        activityForResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                getLocation()
            }
    }

    private fun registerPermission() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                if (result.values.any { !it }) {
                    Toast.makeText(this, "need", Toast.LENGTH_SHORT).show()
                } else {
                    getLocation()
                }
            }
    }

    private fun checkPermission() {
        registerPermission()
        permissionLauncher?.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

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
                .setAspectRatio(Rational(2, 1))
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
                viewBinding.navView.visibility = View.GONE
                enterPictureInPictureMode(it)
            }
        }
        super.onUserLeaveHint()
    }

    private fun checkAlarmPermission() {
        val alarmManager =
            getSystemService(AlarmManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            applicationContext.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }


}