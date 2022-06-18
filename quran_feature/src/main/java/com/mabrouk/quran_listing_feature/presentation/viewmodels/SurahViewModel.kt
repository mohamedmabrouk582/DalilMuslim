package com.mabrouk.quran_listing_feature.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
import com.mabrouk.core.base.BaseViewModel
import com.mabrouk.core.utils.DataStorePreferences
import com.mabrouk.quran_listing_feature.domain.models.QuranReader
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.TafsirAya
import com.mabrouk.quran_listing_feature.domain.models.Verse
import com.mabrouk.quran_listing_feature.domain.usecases.AyaRepositoryUseCases
import com.mabrouk.quran_listing_feature.presentation.utils.*
import com.mabrouk.quran_listing_feature.presentation.states.SurahStates
import com.mabrouk.quran_listing_feature.presentation.utils.AudioDataPass
import com.mabrouk.quran_listing_feature.presentation.workers.AudioDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
@HiltViewModel
class SurahViewModel @Inject constructor(
    val repository: AyaRepositoryUseCases,
    val dataStore: DataStorePreferences
) : BaseViewModel() {
    private val _states = MutableStateFlow<SurahStates>(SurahStates.IDLE)
    val states: StateFlow<SurahStates> = _states
    lateinit var currentReader: QuranReader

    init {
        viewModelScope.launch {
            getCurrentReader()
        }
    }


    fun downloadVerseAudio(context: Context, surahname: String, verses: ArrayList<Verse>) {
        val workManger = WorkManager.getInstance(context)
        val map = verses.map {
            AudioDataPass(
                it.id,
                currentReader.sufix,
                it.chapterId,
                it.verseNumber,
                surahname
            )
        }
        val data = Data.Builder().putString(SURA_LIST_AUDIOS, Gson().toJson(ArrayList(map))).build()
        val request = OneTimeWorkRequest.Builder(AudioDownloader::class.java)
            .setInputData(data)
            .build()
        workManger.enqueue(request)
        _states.value = SurahStates.DownloadVerse(workManger.getWorkInfoByIdLiveData(request.id))
    }

    fun getTafsier(id: Int, verseId: Int): Flow<List<TafsirAya>> =
        repository.getSavedTafsir(id, verseId)

    fun updateSurah(surah: Surah) = viewModelScope.launch { repository.updateSurah(surah) }

    fun getReader(reader: (item: QuranReader) -> Unit) {
        viewModelScope.launch {
            var id = 1
            try {
                id = currentReader.readerId
            } catch (e: Exception) {
                getCurrentReader()
                id = currentReader.readerId

            } finally {
                repository.getReader(id).first().apply {
                    reader(this)
                }
            }

        }
    }

    fun getAllReaders(readers: (data: ArrayList<QuranReader>) -> Unit) {
        viewModelScope.launch {
            readers(repository.getReaders())
        }
    }

    fun updateReader(reader: QuranReader) {
        currentReader = reader
        viewModelScope.launch {
            dataStore.setData(READER_KEY,currentReader)
        }
        _states.value = SurahStates.UpdateReader(currentReader)
    }

    fun updateReader(suraId: Long) {
        Log.d("downloadVerse", currentReader.toString())
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuranReader(currentReader.let {
                if (it.versesIds == null) it.versesIds = arrayListOf()
                it.versesIds?.add(suraId)
                it
            })
            dataStore.setData(READER_KEY,currentReader)
        }
    }



    private suspend fun getCurrentReader() {
        currentReader = dataStore.getData<QuranReader>(READER_KEY).firstOrNull() ?: QuranReader(
            1,
            "عبد الباسط",
            "abdulbasit_abdulsamad_mujawwad/128"
        )
    }

}