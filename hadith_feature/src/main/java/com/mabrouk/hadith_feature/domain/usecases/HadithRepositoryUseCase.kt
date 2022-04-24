package com.mabrouk.hadith_feature.domain.usecases

import com.mabrouk.hadith_feature.domain.models.*
import com.mabrouk.hadith_feature.domain.repository.HadithDefaultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.mabrouk.core.network.Result as Result

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
class HadithRepositoryUseCase @Inject constructor(val repository: HadithDefaultRepository) {

    suspend fun getHadithCategories() : Flow<Result<HadithResponse<ArrayList<HadithCategory>>>>{
      return  repository.getHadithCategories()
    }

    suspend fun savedHadithCategories(data:ArrayList<HadithCategory>){
        repository.savedHadithCategories(data)
    }
    suspend fun saveHadithBook(data: ArrayList<HadithBookNumber>){
        repository.saveHadithBook(data)
    }

    suspend fun saveHadith(data:ArrayList<HadithModel>){
        repository.saveHadith(data)
    }

    suspend fun getHadithBookNumber(collectionName:String) : Flow<Result<HadithResponse<ArrayList<HadithBookNumber>>>>{
        return repository.getHadithBookNumber(collectionName)
    }

    suspend fun getHadith(collectionName:String,bookNumber:String,page:Int) : Flow<Result<HadithResponse<ArrayList<HadithModel>>>>{
        return repository.getHadith(collectionName,bookNumber, page)
    }

    suspend fun getSavedHadithCategories() : Flow<List<HadithCategory>>{
        return repository.getSavedHadithCategories()
    }

    suspend fun getSavedHadithBooks(name:String) : Flow<List<HadithBookNumber>>{
       return repository.getSavedHadithBooks(name)
    }

    suspend fun getSavedHadith(name:String,bookNumber:String) : Flow<List<HadithModel>>{
        return repository.getSavedHadith(name,bookNumber)
    }

}