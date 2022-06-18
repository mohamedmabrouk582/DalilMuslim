package com.mabrouk.quran_listing_feature.data.api

import com.mabrouk.quran_listing_feature.domain.models.TafsirAya
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
interface TafseerApi {
    @GET("tafseer/{tafseer_id}/{sura_number}/{ayah_number}")
    fun getAyaTafseer(@Path("tafseer_id") tafseerId:Int, @Path("sura_number") suraNumber:Int,
                      @Path("ayah_number") ayahNumber:Int) : Deferred<TafsirAya>
}