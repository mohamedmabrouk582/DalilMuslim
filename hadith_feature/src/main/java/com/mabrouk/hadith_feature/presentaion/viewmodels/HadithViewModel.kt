package com.mabrouk.hadith_feature.presentaion.viewmodels

import androidx.lifecycle.viewModelScope
import com.mabrouk.core.base.BaseViewModel
import com.mabrouk.core.utils.DataStorePreferences
import com.mabrouk.hadith_feature.domain.usecases.HadithRepositoryUseCase
import com.mabrouk.hadith_feature.presentaion.HADITH_CATEGORIES_DOWNLOADED
import com.mabrouk.hadith_feature.presentaion.states.HadithStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.mabrouk.core.network.Result as Result

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
@HiltViewModel
class HadithViewModel @Inject constructor(
    val repository: HadithRepositoryUseCase,
    val dataStore: DataStorePreferences
) : BaseViewModel() {
    private val _states = MutableStateFlow<HadithStates>(HadithStates.Idle)
    val states: StateFlow<HadithStates> = _states
    private var requestType = RequestType.HADITH_CATEGORY
    private lateinit var collectionName: String
    private lateinit var bookNumber: String

    fun getHadithCategories() {
        requestType = RequestType.HADITH_CATEGORY
        viewModelScope.launch {
            if (dataStore.getBoolean(HADITH_CATEGORIES_DOWNLOADED)) {
                repository.getSavedHadithCategories().collect {
                    _states.value = HadithStates.LoadCategories(ArrayList(it))
                }
            } else {
                _states.value = HadithStates.Idle
                repository.getHadithCategories().collect {
                    when (it) {
                        is Result.NoInternetConnect -> error.set(it.error)
                        is Result.OnFailure -> error.set(it.throwable.message ?: "")
                        is Result.OnLoading -> loader.set(true)
                        is Result.OnSuccess -> {
                            _states.value = HadithStates.LoadCategories(it.data.data)
                            withContext(Dispatchers.IO) {
                                repository.savedHadithCategories(it.data.data)
                                dataStore.setBoolean(HADITH_CATEGORIES_DOWNLOADED, true)
                            }
                        }
                        Result.OnFinish -> loader.set(false)
                    }
                }
            }
        }
    }

    fun loadHadithBook(name: String) {
        collectionName = name
        requestType = RequestType.HADITH_BOOK
        viewModelScope.launch {
            val savedHadithBooks = repository.getSavedHadithBooks(name).first()
            if (savedHadithBooks.isNotEmpty()) {
                _states.value = HadithStates.LoadHadithBooks(ArrayList(savedHadithBooks))
            } else {
                repository.getHadithBookNumber(name).collect {
                    when (it) {
                        is Result.NoInternetConnect -> error.set(it.error)
                        is Result.OnFailure -> error.set(it.throwable.message ?: "")
                        is Result.OnLoading -> loader.set(true)
                        is Result.OnSuccess -> {
                            val books = ArrayList(it.data.data.map {
                                it.collectionName = name
                                it
                            })
                            _states.value = HadithStates.LoadHadithBooks(books)
                            withContext(Dispatchers.IO) {
                                repository.saveHadithBook(books)
                            }
                        }
                        Result.OnFinish -> loader.set(false)
                    }
                }
            }
        }
    }

    fun loadHadiths(name: String, bookNumber: String, page: Int? = null) {
        collectionName = name
        this.bookNumber = bookNumber
        requestType = RequestType.HADITH
        viewModelScope.launch {
            val hadith = repository.getSavedHadith(name, bookNumber).first()
            if (hadith.isNotEmpty() && page == null) {
                _states.value = HadithStates.LoadHadith(ArrayList(hadith))
            } else {
                repository.getHadith(name, bookNumber, page ?: 1).collect {
                    when (it) {
                        is Result.NoInternetConnect -> error.set(it.error)
                        is Result.OnFailure -> error.set(it.throwable.message ?: "")
                        is Result.OnLoading -> if (page?:1==1) loader.set(true)
                        is Result.OnSuccess -> {
                            _states.value = HadithStates.LoadHadith(it.data.data)
                            withContext(Dispatchers.IO) {
                                repository.saveHadith(it.data.data)
                            }
                            if (it.data.next != null) {
                                loadHadiths(name, bookNumber, it.data.next)
                            }
                        }
                        Result.OnFinish -> loader.set(false)
                    }
                }
            }
        }
    }


    private enum class RequestType {
        HADITH, HADITH_BOOK, HADITH_CATEGORY
    }

}