package com.mabrouk.history_feature.peresntaion.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.SparseArray
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.common.reflect.TypeToken
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.mabrouk.core.base.BaseViewModel
import com.mabrouk.core.network.CheckNetwork
import com.mabrouk.history_feature.R
import com.mabrouk.history_feature.domain.models.Story
import com.mabrouk.history_feature.domain.usecase.StoryRepositoryUseCase
import com.mabrouk.history_feature.peresntaion.VIDEO_KEY
import com.mabrouk.history_feature.peresntaion.states.StoryStates
import com.mabrouk.history_feature.peresntaion.workers.VideoDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/20/22
 */
@HiltViewModel
class StoriesViewModel @Inject constructor(
    val  context: Application,
    val repository: StoryRepositoryUseCase,
    val remoteConfig: FirebaseRemoteConfig
) : BaseViewModel() {

    private val _states = MutableStateFlow<StoryStates>(StoryStates.Idle)
    val states: StateFlow<StoryStates> = _states

    fun loadStories() {
        if (CheckNetwork.isOnline(context)) {
            getRemoteData()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
//            viewModelScope.launch {
//               repository.getStories().first().forEach {
//                   _states.value = StoryStates.AddStory(it)
//
//               }
//            }
        }
    }

    private fun getRemoteData() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val type = object : TypeToken<ArrayList<Story>>() {}.type
                    val tt = remoteConfig.getString("videos_ids")
                    val data: ArrayList<Story> = Gson().fromJson(
                        tt, type
                    )
                    viewModelScope.launch {
                        // val stories = repository.getStories().first()
                        data.forEach { story ->
//                            val item = stories.find { it.video_key == story.video_key }
//                            if (item!=null){
//                                _states.value = StoryStates.AddStory(item)
//                            }else{
                            getYoutubeUrl(story.video_key)
                            //  }
                        }
                    }

                    _states.value = StoryStates.ShowNotification
                }

            }
    }

    @SuppressLint("StaticFieldLeak")
    fun getYoutubeUrl(key: String) {
        object : YouTubeExtractor(context) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles?.get(18)?.url != null) {
                    val story = Story(
                        key,
                        ytFiles.get(18)?.url ?: "",
                        videoMeta?.title ?: "",
                        videoMeta?.hqImageUrl ?: "",
                        ytFiles.get(18)?.format?.ext ?: "mp4"
                    )
                    _states.value = StoryStates.AddStory(story)
                }
//                viewModelScope.launch(Dispatchers.IO) {
//                    repository.insertStory(story)
//                }
            }
        }.extract("http://youtube.com/watch?v=${key}")
    }

    override fun onCleared() {
        _states.value = StoryStates.Idle
        super.onCleared()
    }

    fun downloadVideo(model: Story) {
        val workManger = WorkManager.getInstance(context)
        val data = Data.Builder().putString(VIDEO_KEY, Gson().toJson(model)).build()
        val worker = OneTimeWorkRequest.Builder(VideoDownloader::class.java)
            .setInputData(data)
            .build()
        workManger.enqueue(worker)
        _states.value = StoryStates.DownloadVideo(workManger.getWorkInfoByIdLiveData(worker.id))
    }

}