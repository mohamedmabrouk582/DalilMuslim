package com.mabrouk.history_feature.peresntaion.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.navigation.dynamicfeatures.Constants
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.mabrouk.core.network.Result.*
import com.mabrouk.core.utils.FileUtils
import com.mabrouk.history_feature.domain.models.Story
import com.mabrouk.history_feature.domain.usecase.StoryRepositoryUseCase
import com.mabrouk.history_feature.peresntaion.VIDEO_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@HiltWorker
class VideoDownloader @AssistedInject constructor (
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    val repository: StoryRepositoryUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        var result: Result = Result.failure()
        val storyEntity = Gson().fromJson(inputData.getString(VIDEO_KEY) ?: "", Story::class.java)
        repository.downloadAudio(storyEntity.url).collectLatest {
            when (it) {
                is NoInternetConnect -> {
                    result = Result.failure()
                }
                is OnFailure -> {
                    result = Result.failure()
                }
                is OnSuccess -> {
                    FileUtils.saveVideo(it.data, storyEntity.title, storyEntity.ext)
                    result = Result.success()
                }
                else -> {
                    Log.d("Tag","")
                }
            }
        }
        return result
    }


}