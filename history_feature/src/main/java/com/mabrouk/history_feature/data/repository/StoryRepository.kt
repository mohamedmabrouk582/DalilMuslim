package com.mabrouk.history_feature.data.repository

import android.content.Context
import com.mabrouk.core.network.Result
import com.mabrouk.core.network.executeCall
import com.mabrouk.history_feature.data.db.StoryApi
import com.mabrouk.history_feature.data.db.StoryDao
import com.mabrouk.history_feature.domain.models.Story
import com.mabrouk.history_feature.domain.repository.StoryDefaultRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
class StoryRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val api: StoryApi,
    private val dao: StoryDao
) : StoryDefaultRepository {
    override fun downloadAudio(url: String): Flow<Result<ResponseBody>> {
        return executeCall(context) { api.downloadAudio(url) }
    }

    override suspend fun insertStory(storyEntity: Story): Long {
        return dao.insertStory(storyEntity)
    }

    override fun getStories(): Flow<List<Story>> {
        return dao.getStories()
    }

    override fun searchStory(query: String): Flow<Story> {
        return dao.search(query)
    }
}