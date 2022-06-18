package com.mabrouk.quran_listing_feature.data.api

import com.mabrouk.quran_listing_feature.domain.response.JuzResponse
import com.mabrouk.quran_listing_feature.domain.response.SurahResponse
import com.mabrouk.quran_listing_feature.domain.response.TafsirResponse
import com.mabrouk.quran_listing_feature.domain.response.VersesResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
interface QuranApi {
    @GET("chapters")
    suspend fun getAllSurah() : Response<SurahResponse>

    @GET("juzs")
    suspend fun getJuzs() : Response<JuzResponse>

    @GET("chapters/{chapter_id}/verses?recitation=1&translations=21&language=en&text_type=words")
    suspend fun getSurahVerses(@Path("chapter_id") chapterId:Int, @Query("page") page:Int) : Response<VersesResponse>

    @GET("chapters/{chapter_id}/verses/{verse_id}/tafsirs")
    suspend fun getVerseTafsir(@Path("chapter_id") chapterId:Int, @Path("verse_id") verseId:Int) : Response<TafsirResponse>

    @GET
    @Streaming
    suspend fun downloadAudio(@Url url:String) : Response<ResponseBody>

    @GET("search?size=20&page=1&language=ar")
    suspend fun search(@Query("q") query:String) : Response<Any>
}