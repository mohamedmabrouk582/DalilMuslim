package com.mabrouk.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 9/29/22
 */
class DefaultLocationClient(
    private val context: Context
) : LocationClient {

    val client by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private var locationCallback: LocationCallback? = null

    fun close() {
        locationCallback?.let {
            client.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(
        interval: Long,
        locationError: (type: LocationError) -> Unit
    ): Flow<Location> {
        return callbackFlow {
            if (!context.hasPermission())
                locationError(LocationError.PERMISSION)

            if (checkLocation())
                locationError(LocationError.GPS)

            val request = LocationRequest.Builder(interval)
                .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let {
                        launch { send(it) }
                    }
                }
            }


            client.requestLocationUpdates(
                request,
                locationCallback!!,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback!!)
            }

        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(locationError: (type: LocationError) -> Unit): Flow<Location> {
        return callbackFlow {
            if (!context.hasPermission())
                locationError(LocationError.PERMISSION)

            if (checkLocation())
                locationError(LocationError.GPS)

            client.lastLocation.addOnSuccessListener {
                it?.let {
                    launch { send(it) }
                }
            }

            awaitClose {}
        }
    }


    private fun checkLocation(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return !isGpsEnable && !isNetworkEnable
    }
}
