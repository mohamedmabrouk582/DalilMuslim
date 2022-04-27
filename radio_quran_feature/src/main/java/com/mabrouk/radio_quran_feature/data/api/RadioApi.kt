package com.mabrouk.radio_quran_feature.data.api

import com.mabrouk.radio_quran_feature.domain.models.RadioResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
interface RadioApi {
    @GET
    suspend fun getRadios(@Url url : String) : Response<RadioResponse>
}