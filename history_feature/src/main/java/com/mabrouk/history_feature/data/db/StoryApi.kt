package com.mabrouk.history_feature.data.db

import com.mabrouk.history_feature.domain.models.Story
import kotlinx.coroutines.Deferred
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

    @GET
    fun getStory(@Url url : String) : Deferred<Story>
}