package com.example.prayertime.data.api

import com.example.prayertime.domian.models.PrayerResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/8/23
 */
interface PrayerApi {
    @GET("v1/calendar")
    fun getPrayerTimings(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Deferred<PrayerResponse>
}