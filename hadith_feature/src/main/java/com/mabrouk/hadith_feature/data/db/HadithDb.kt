package com.mabrouk.hadith_feature.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabrouk.hadith_feature.domain.models.HadithBookNumber
import com.mabrouk.hadith_feature.domain.models.HadithCategory
import com.mabrouk.hadith_feature.domain.models.HadithModel

@Database(
    entities = [HadithCategory::class, HadithBookNumber::class, HadithModel::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DataConverter::class)
abstract class HadithDb : RoomDatabase() {
    abstract fun getHadithDao(): HadithDao
}