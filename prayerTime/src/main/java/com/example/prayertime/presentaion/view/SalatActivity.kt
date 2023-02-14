package com.example.prayertime.presentaion.view

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.prayertime.R
import com.example.prayertime.databinding.SalatLayoutBinding
import com.example.prayertime.presentaion.qibla.CompassQibla
import dagger.hilt.android.AndroidEntryPoint

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/11/23
 */
@AndroidEntryPoint
class SalatActivity : AppCompatActivity() {
    lateinit var viewBinding: SalatLayoutBinding
    private var currentCompassDegree = 0f
    private var currentNeedleDegree = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.salat_layout)

        CompassQibla.Builder(this).onDirectionChangeListener { qiblaDirection ->
            viewBinding.tvDirection.text =
                if (qiblaDirection.isFacingQibla) {
                    viewBinding.image.setImageResource(R.drawable.sh_)
                    getString(R.string.txt_qibla)
                } else {
                    viewBinding.image.setImageResource(R.drawable.sh)
                    "${qiblaDirection.needleAngle.toInt()}°"
                }

            val rotateCompass = RotateAnimation(
                currentCompassDegree, qiblaDirection.compassAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentCompassDegree = qiblaDirection.compassAngle

            viewBinding.ivCompass.startAnimation(rotateCompass)

            val rotateNeedle = RotateAnimation(
                currentNeedleDegree, qiblaDirection.needleAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentNeedleDegree = qiblaDirection.needleAngle

            viewBinding.ivNeedle.startAnimation(rotateNeedle)
        }.build()
    }

    fun close(view: View) {
        finish()
    }


}