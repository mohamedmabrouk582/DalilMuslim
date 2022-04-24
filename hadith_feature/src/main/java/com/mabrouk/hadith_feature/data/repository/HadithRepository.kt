package com.mabrouk.hadith_feature.data.repository

import android.content.Context
import com.mabrouk.core.network.executeCall
import com.mabrouk.hadith_feature.data.api.HadithApi
import com.mabrouk.hadith_feature.data.db.HadithDao
import com.mabrouk.hadith_feature.domain.models.HadithBookNumber
import com.mabrouk.hadith_feature.domain.models.HadithCategory
import com.mabrouk.hadith_feature.domain.models.HadithModel
import com.mabrouk.hadith_feature.domain.models.HadithResponse
import com.mabrouk.hadith_feature.domain.repository.HadithDefaultRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.mabrouk.core.network.Result as Result

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
class HadithRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val api: HadithApi,
    val dao: HadithDao
) : HadithDefaultRepository {

    override suspend fun getHadithCategories(): Flow<Result<HadithResponse<ArrayList<HadithCategory>>>> {
        return executeCall(context) { api.getHadithCategoriesAsync() }
    }

    override suspend fun savedHadithCategories(data: ArrayList<HadithCategory>) {
        dao.insertHadithCategories(data)
    }

    override suspend fun saveHadithBook(data: ArrayList<HadithBookNumber>) {
        dao.insertHadithBookNumbers(data)
    }

    override suspend fun saveHadith(data: ArrayList<HadithModel>) {
        dao.insertHadith(data)
    }

    override suspend fun getHadithBookNumber(collectionName: String): Flow<Result<HadithResponse<ArrayList<HadithBookNumber>>>> {
        return executeCall(context) { api.getHadithBooks(collectionName) }
    }

    override suspend fun getHadith(
        collectionName: String,
        bookNumber: String,
        page: Int
    ): Flow<Result<HadithResponse<ArrayList<HadithModel>>>> {
        return executeCall(context) { api.getHadith(collectionName, bookNumber, page) }
    }

    override suspend fun getSavedHadithCategories(): Flow<List<HadithCategory>> {
        return dao.getHadithCategories()
    }

    override suspend fun getSavedHadithBooks(name: String): Flow<List<HadithBookNumber>> {
        return dao.getHadithBooks(name)
    }

    override suspend fun getSavedHadith(name: String, bookNumber: String): Flow<List<HadithModel>> {
        return dao.getHadiths(name, bookNumber)
    }
}