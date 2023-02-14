package com.mabrouk.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 9/29/22
 */

fun Context.hasPermission() =
    ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


fun LocationClient.getMyCurrentLocation(
    scope: CoroutineScope,
    locationError: (type: LocationError) -> Unit = {},
    location: (location: Location) -> Unit
) {
    this.getCurrentLocation(locationError)
        .onEach {
            location(it)
        }.launchIn(scope)
}


fun LocationClient.getMyUpdatedLocation(
    scope: CoroutineScope,
    intervel: Long,
    locationError: (type: LocationError) -> Unit = {},
    location: (location: Location) -> Unit
) {

    this.getLocationUpdates(intervel, locationError)
        .onEach {
            location(it)
        }.launchIn(scope)
}

interface LocationClient {
    fun getLocationUpdates(interval : Long,locationError:(type : LocationError)->Unit ) : Flow<Location>
    fun getCurrentLocation(locationError:(type : LocationError)->Unit) : Flow<Location>
}

enum class LocationError(type : String){
    PERMISSION("Permissions are denied") , GPS("GPS not enable")
}