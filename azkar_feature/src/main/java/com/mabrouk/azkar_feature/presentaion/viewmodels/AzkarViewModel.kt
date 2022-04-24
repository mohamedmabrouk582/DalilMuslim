package com.mabrouk.azkar_feature.presentaion.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mabrouk.azkar_feature.domain.models.Azkar
import com.mabrouk.azkar_feature.domain.usecases.AzkarUseCase
import com.mabrouk.core.base.BaseViewModel
import com.mabrouk.core.network.toArrayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@HiltViewModel
class AzkarViewModel @Inject constructor(val useCase: AzkarUseCase) : BaseViewModel() {

    suspend fun loadCategories(): ArrayList<String> {
        val data = ArrayList(HashSet<String>(useCase.getAzkarCategories().first()).toList())
        data.sort()
        return data
    }

    suspend fun getAzkar(category: String): ArrayList<Azkar> {
        return useCase.getAzkarBYCategory(category).first().toArrayList()
    }

}