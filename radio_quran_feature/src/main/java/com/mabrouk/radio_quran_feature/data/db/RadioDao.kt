package com.mabrouk.radio_quran_feature.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mabrouk.radio_quran_feature.domain.models.Radio
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/26/22
 */
@Dao
interface RadioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRadios(radios: ArrayList<Radio>)

    @Query("select * from radio")
    fun getRadios(): Flow<List<Radio>>

}