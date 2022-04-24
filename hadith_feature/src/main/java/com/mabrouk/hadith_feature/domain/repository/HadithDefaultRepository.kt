package com.mabrouk.hadith_feature.domain.repository

import com.mabrouk.hadith_feature.domain.models.HadithBookNumber
import com.mabrouk.hadith_feature.domain.models.HadithCategory
import com.mabrouk.hadith_feature.domain.models.HadithModel
import com.mabrouk.hadith_feature.domain.models.HadithResponse
import kotlinx.coroutines.flow.Flow
import com.mabrouk.core.network.Result as Result

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
interface HadithDefaultRepository {
    suspend fun getHadithCategories() : Flow<Result<HadithResponse<ArrayList<HadithCategory>>>>
    suspend fun savedHadithCategories(data:ArrayList<HadithCategory>)
    suspend fun saveHadithBook(data: ArrayList<HadithBookNumber>)
    suspend fun saveHadith(data:ArrayList<HadithModel>)
    suspend fun getHadithBookNumber(collectionName:String) : Flow<Result<HadithResponse<ArrayList<HadithBookNumber>>>>
    suspend fun getHadith(collectionName:String,bookNumber:String,page:Int) : Flow<Result<HadithResponse<ArrayList<HadithModel>>>>
    suspend fun getSavedHadithCategories() : Flow<List<HadithCategory>>
    suspend fun getSavedHadithBooks(name:String) : Flow<List<HadithBookNumber>>
    suspend fun getSavedHadith(name:String,bookNumber:String) : Flow<List<HadithModel>>

}