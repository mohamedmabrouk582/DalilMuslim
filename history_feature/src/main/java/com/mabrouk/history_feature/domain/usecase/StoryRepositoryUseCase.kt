package com.mabrouk.history_feature.domain.usecase

import com.mabrouk.core.network.Result
import com.mabrouk.history_feature.domain.models.Story
import com.mabrouk.history_feature.domain.repository.StoryDefaultRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
class StoryRepositoryUseCase @Inject constructor(val repository: StoryDefaultRepository) {

     fun downloadAudio(url: String): Flow<Result<ResponseBody>> {
        return repository.downloadAudio(url)
    }

    suspend fun insertStory(storyEntity: Story) : Long {
        return repository.insertStory(storyEntity)
    }
    fun getStories() : Flow<List<Story>>{
        return repository.getStories()
    }

    fun searchStory(query:String) : Flow<Story>{
        return repository.searchStory(query)
    }

}