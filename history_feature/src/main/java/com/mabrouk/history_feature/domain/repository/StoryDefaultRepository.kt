package com.mabrouk.history_feature.domain.repository

import com.mabrouk.core.network.Result
import com.mabrouk.history_feature.domain.models.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
interface StoryDefaultRepository {
    fun downloadAudio(url:String) : Flow<Result<ResponseBody>>
    suspend fun insertStory(storyEntity: Story) : Long
    fun getStories() : Flow<List<Story>>
    fun searchStory(query:String) : Flow<Story>
}