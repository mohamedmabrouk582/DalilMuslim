package com.mabrouk.quran_listing_feature.presentation.viewmodels

import android.content.Context
import android.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.reflect.TypeToken
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.mabrouk.core.base.BaseViewModel
import com.mabrouk.core.network.Result
import com.mabrouk.core.network.toArrayList
import com.mabrouk.core.utils.DataStorePreferences
import com.mabrouk.quran_listing_feature.domain.models.Juz
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.Verse
import com.mabrouk.quran_listing_feature.domain.respository.QuranDefaultRepository
import com.mabrouk.quran_listing_feature.domain.usecases.QuranRepositoryUseCase
import com.mabrouk.quran_listing_feature.presentation.utils.*
import com.mabrouk.quran_listing_feature.presentation.states.QuranStates
import com.mabrouk.quran_listing_feature.presentation.workers.SurahDownloadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@HiltViewModel
class QuranViewModel @Inject constructor(
    val repository: QuranRepositoryUseCase,
    val remoteConfig: FirebaseRemoteConfig,
    val dataStore: DataStorePreferences
) : BaseViewModel() {

    private var _quranStates = MutableStateFlow<QuranStates>(QuranStates.IDLE)
    val quranStates: StateFlow<QuranStates> = _quranStates
    var searchJob: Job? = null
    var juzs: ArrayList<Juz> = ArrayList()
    var surahs: ArrayList<Surah> = ArrayList()

    fun loadData() {
        viewModelScope.launch {
            if (dataStore.getBoolean(SURAH_LIST_DOWNLOADS)) {

                viewModelScope.launch {
                    repository.getSavedJuz().collect {
                        juzs = it.toArrayList()
                        if (surahs.isNotEmpty()) {
                            _quranStates.value = QuranStates.LoadJuzSurahs(mapJuz(juzs, surahs))
                        }
                    }
                }

                viewModelScope.launch {
                    repository.getSavedSurah().collect {
                        surahs = it.toArrayList()
                        if (juzs.isNotEmpty()) {
                            _quranStates.value = QuranStates.LoadJuzSurahs(mapJuz(juzs, surahs))
                        }
                    }
                }


            } else {
                requestJuz()
                requestSurahs()
            }
        }

        getReaders()

    }

     fun requestJuz() {
        viewModelScope.launch {
            repository.requestJuz().collect {
                when (it) {
                    is Result.OnFinish -> {
                        loader.set(false)
                    }
                    is Result.NoInternetConnect -> {
                        error.set(it.error)
                        _quranStates.value = QuranStates.Error(it.error)
                    }
                    is Result.OnFailure -> {
                        error.set(it.throwable.message)
                        _quranStates.value = QuranStates.Error(it.throwable.message.toString())
                    }
                    is Result.OnLoading -> {
                        loader.set(true)
                    }
                    is Result.OnSuccess -> {
                        juzs = it.data.juzs
                        if (surahs.isNotEmpty()) {
                            _quranStates.value = QuranStates.LoadJuzSurahs(mapJuz(juzs, surahs))
                        }
                        repository.saveJuz(it.data.juzs)
                    }
                }
            }
        }
    }

     fun requestSurahs() {
        viewModelScope.launch {
            repository.requestSurahs().collect {
                when (it) {
                    is Result.OnFinish -> {
                        loader.set(false)
                    }
                    is Result.NoInternetConnect -> {
                        error.set(it.error)
                        _quranStates.value = QuranStates.Error(it.error)
                    }
                    is Result.OnFailure -> {
                        error.set(it.throwable.message)
                        _quranStates.value = QuranStates.Error(it.throwable.message.toString())
                    }
                    is Result.OnLoading -> {
                        loader.set(true)
                    }
                    is Result.OnSuccess -> {
                        surahs = it.data.chapters
                        if (juzs.isNotEmpty()) {
                            _quranStates.value = QuranStates.LoadJuzSurahs(mapJuz(juzs, surahs))
                        }
                        repository.saveSurahs(it.data.chapters)
                    }
                }
            }
        }
    }

    val searchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            _quranStates.value = QuranStates.SearchResult(query ?: "")
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                MutableStateFlow(newText)
                    .debounce(400)
                    .distinctUntilChanged()
                    .flowOn(Dispatchers.Default)
                    .collect {
                        _quranStates.value = QuranStates.SearchResult(newText ?: "")
                    }
            }

            return false
        }

    }

    fun surahListDownloads(bool: Boolean = true) {
        viewModelScope.launch { dataStore.setBoolean(SURAH_LIST_DOWNLOADS, bool) }
    }

    /**
     * for download all verse data
     */
    fun downloadJuz(ids: List<Int>, context: Context): LiveData<WorkInfo> {
        val putIntArray =
            Data.Builder().putIntArray(VERSES_IDS, ids.map { it }.toIntArray())
        val build = OneTimeWorkRequest.Builder(SurahDownloadWorker::class.java)
            .setInputData(putIntArray.build())
            .build()

        val instance = WorkManager.getInstance(context)
        instance.enqueue(build)
        return instance.getWorkInfoByIdLiveData(build.id)
    }

    suspend fun updateSurah(sura: Surah) {
        repository.updateSura(sura)
    }

    suspend fun updateJuz(juz: Juz) {
        repository.updateJuz(juz)
    }

    fun fetchVerse(id: Int): Flow<List<Verse>> {
        return repository.getSavedVerses(id)
    }

    fun getReaders() {
        viewModelScope.launch {
            if (!dataStore.getBoolean(READERS_DOWNLOADS)) {
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val type = object : TypeToken<ArrayList<QuranReader>>() {}.type
                            val tt = remoteConfig.getString("quran_readers")
                            val data: ArrayList<QuranReader> = Gson().fromJson(
                                tt, type
                            )
                            viewModelScope.launch(Dispatchers.IO) {
                                repository.insertReaders(data)
                                dataStore.setBoolean(READERS_DOWNLOADS, true)
                            }
                        }

                    }
            }
        }
    }

}
