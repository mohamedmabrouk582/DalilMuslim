package com.mabrouk.quran_listing_feature.remote

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
class MockServerDispatcher {
    fun getJsonContent(fileName: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
    }

    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/juzs" -> MockResponse().setResponseCode(200).setBody(getJsonContent("juzs.json"))
                "/chapters" -> MockResponse().setResponseCode(200).setBody(getJsonContent("surahs.json"))
                "/chapters/1/verses?recitation=1&translations=21&language=en&text_type=words&page=1" -> MockResponse().setResponseCode(200).setBody(getJsonContent("verse1.json"))
                else -> MockResponse().setResponseCode(400)
            }
        }
    }


}