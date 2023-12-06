package com.mabrouk.prayertime.presentaion.qibla

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Address
import android.location.Location
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mabrouk.prayertime.presentaion.qibla.LocationUtils.checkLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mabrouk.core.utils.NOTIFICATION_CHANNEL

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/14/23
 */
class CompassQibla {
    class Builder(private val activity: AppCompatActivity) : SensorEventListener {

        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var currentLocation: Location
        private lateinit var sensorManager: SensorManager
        private  var sensor: Sensor? = null
        private var currentDegree = 0f
        private var currentDegreeNeedle = 0f
        private val model: CompassQiblaViewModel =
            ViewModelProvider(activity).get(CompassQiblaViewModel::class.java)

        @SuppressLint("MissingPermission")
        fun build() {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            activity.checkLocationPermission { requestLocationPermission() }
            fusedLocationClient.lastLocation.addOnSuccessListener {
                it?.let { location ->
                    currentLocation = location
                    model.getLocationAddress(activity, currentLocation)
                    sensorManager =
                        activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
                    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
                    sensorManager.registerListener(
                        this, sensor, SensorManager.SENSOR_DELAY_GAME
                    )
                }
            }
        }

        fun onPermissionGranted(onGranted: (permission: String) -> Unit) = apply {
            model.permission.observe(activity) { if (it.first) onGranted(it.second) }
        }

        fun onPermissionDenied(onDenied: () -> Unit) = apply {
            model.permission.observe(activity) { if (!it.first) onDenied() }
        }

        fun onDirectionChangeListener(onChange: (qiblaDirection: QiblaDirection) -> Unit) = apply {
            model.direction.observe(activity) { onChange(it) }
        }

        fun onGetLocationAddress(onGetLocation: (address: Address) -> Unit) = apply {
            model.locationAddress.observe(activity) { onGetLocation(it) }
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val degree = event?.values?.get(0) ?: 0f
            val destinationLoc = Location("service Provider").apply {
                latitude = 21.422487
                longitude = 39.826206
            }

            var bearTo: Float = currentLocation.bearingTo(destinationLoc)
            if (bearTo < 0) bearTo += 360
            var direction: Float = bearTo - degree
            if (direction < 0) direction += 360

            val isFacingQibla = direction in 357.0..360.0 || direction in 0.0..3.0

            currentDegreeNeedle = direction
            currentDegree = -degree
            val qiblaDirection = QiblaDirection(-degree, direction, isFacingQibla)
            model.updateCompassDirection(qiblaDirection)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Log.d(NOTIFICATION_CHANNEL,accuracy.toString())
        }

        @SuppressLint("NewApi")
        fun requestLocationPermission() {
            val locationPermissionRequest = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                LocationUtils.handlePermission(
                    permissions,
                    { model.onPermissionUpdate(true, it) },
                    { model.onPermissionUpdate(false) }
                )
            }
            LocationUtils.launchPermission(locationPermissionRequest)
        }
    }
}