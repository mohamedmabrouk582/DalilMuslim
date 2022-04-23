package com.mabrouk.history_feature.peresntaion.states

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.mabrouk.history_feature.domain.models.Story

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/20/22
 */
sealed class StoryStates{
    object Idle : StoryStates()
    object ShowNotification : StoryStates()
    data class AddStory(val storyEntity: Story) : StoryStates()
    data class Error(val error:String) : StoryStates()
    data class DownloadVideo(val workInfo: LiveData<WorkInfo>) : StoryStates()
}
