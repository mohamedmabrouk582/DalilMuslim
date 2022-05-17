package com.mabrouk.radio_quran_feature.presentaion.viewmodels

import androidx.lifecycle.viewModelScope
import com.mabrouk.core.base.BaseViewModel
import com.mabrouk.core.network.Result.*
import com.mabrouk.core.utils.DataStorePreferences
import com.mabrouk.radio_quran_feature.domain.usecases.RadioUseCase
import com.mabrouk.radio_quran_feature.presentaion.RADIOS_DOWNLOADS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
@HiltViewModel
class RadioViewModel @Inject constructor(
    val repository: RadioUseCase,
    val dataStore: DataStorePreferences
) : BaseViewModel() {

    private val _states = MutableStateFlow<RadioStates>(RadioStates.Idle)
    var states: StateFlow<RadioStates> = _states

    fun requestRadios() {
        viewModelScope.launch {
            if (dataStore.getBoolean(RADIOS_DOWNLOADS)) {
                _states.value = RadioStates.LoadData(repository.getSavedRadios())
            } else {
                repository.requestRadios().collect {
                    when (it) {
                        is NoInternetConnect -> {
                            _states.value = RadioStates.LoadData(repository.getSavedRadios())
                        }
                        is OnSuccess -> {
                            _states.value = RadioStates.LoadData(it.data.radios)
                            repository.saveRadios(it.data.radios)
                            dataStore.setBoolean(RADIOS_DOWNLOADS, true)
                        }
                    }
                }
            }
        }
    }

}