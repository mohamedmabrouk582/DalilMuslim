package com.mabrouk.quran_listing_feature.presentation.states

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.mabrouk.quran_listing_feature.domain.models.QuranReader

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
sealed class SurahStates{
    object IDLE : SurahStates()
    data class DownloadVerse(val workInfo: LiveData<WorkInfo>) : SurahStates()
    data class UpdateReader(val readerEntity: QuranReader) : SurahStates()
}
