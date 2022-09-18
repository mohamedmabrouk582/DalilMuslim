package com.mabrouk.quran_listing_feature.presentation.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mabrouk.core.network.Result.*
import com.mabrouk.core.utils.EventBus
import com.mabrouk.quran_listing_feature.domain.usecases.QuranRepositoryUseCase
import com.mabrouk.quran_listing_feature.presentation.utils.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
@HiltWorker
class SurahDownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    val repository: QuranRepositoryUseCase,
    val event: EventBus
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        var result: String? = null
        val data = Data.Builder()
        inputData.getIntArray(VERSES_IDS)?.toList()?.forEach {
            repository.getSurahById(it)
                .firstOrNull()?.nameArabic?.let { it1 -> event.sendType(it1) }
            result = downloadSurah(it)
        }
        data.putString(DOWNLOAD_VERSES_IDS, result)
        if (result == null) return Result.success(data.build())
        return Result.failure(data.build())
    }

    private suspend fun downloadSurah(id: Int, page: Int = 1): String? {
        var result: String? = null
        repository.requestVerses(id, page).collect {
            when (it) {
                is OnSuccess -> {
                    repository.saveVerses(it.data.verses)
                    if (it.data.meta.currentPage <= it.data.meta.totalPages!!) {
                        downloadSurah(id, it.data.meta.currentPage + 1)
                    }
                }
                is OnFailure -> result = it.throwable.message!!
                is NoInternetConnect -> result = it.error
                else -> {}
            }
        }
        return result
    }
}