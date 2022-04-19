package com.mabrouk.quran_listing_feature.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabrouk.quran_listing_feature.domain.models.*

@Database(
    entities = [
        Surah::class,
        Juz::class,
        Verse::class,
        TafsirAya::class,
        QuranReader::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DbConverter::class)
abstract class QuranDB : RoomDatabase() {
    abstract fun getQuranDao(): QuranDao
}