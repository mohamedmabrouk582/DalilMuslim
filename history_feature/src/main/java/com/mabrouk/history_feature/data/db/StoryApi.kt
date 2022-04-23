package com.mabrouk.history_feature.data.db

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/20/22
 */
interface StoryApi {
    @GET
    @Streaming
    suspend fun downloadAudio(@Url url:String) : Response<ResponseBody>
}