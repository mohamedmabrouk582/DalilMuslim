package com.mabrouk.hadith_feature.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mabrouk.hadith_feature.domain.models.HadithBookNumber
import com.mabrouk.hadith_feature.domain.models.HadithCategory
import com.mabrouk.hadith_feature.domain.models.HadithModel
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/23/22
 */
@Dao
interface HadithDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHadithCategories(categories:ArrayList<HadithCategory>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHadithBookNumbers(books:ArrayList<HadithBookNumber>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHadith(data:ArrayList<HadithModel>)


    @Query("select * from HadithCategory")
    fun getHadithCategories() : Flow<List<HadithCategory>>

    @Query("select * from HadithBookNumber where collectionName =:name")
    fun getHadithBooks(name:String) : Flow<List<HadithBookNumber>>

    @Query("Select * from HadithModel where collection =:collection and bookNumber=:bookNumber")
    fun getHadiths(collection:String,bookNumber:String) : Flow<List<HadithModel>>
}