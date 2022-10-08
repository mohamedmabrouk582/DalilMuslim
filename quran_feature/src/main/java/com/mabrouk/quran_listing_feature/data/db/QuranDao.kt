package com.mabrouk.quran_listing_feature.data.db

import androidx.room.*
import com.mabrouk.quran_listing_feature.domain.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReaders(readers : ArrayList<QuranReader>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSurahs(suras:ArrayList<Surah>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJuzs(juzs:ArrayList<Juz>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVerses(vers:ArrayList<Verse>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVerseTafsir(tafsirEntities:TafsirAya)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateJUz(juz:Juz)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateVerse(verse:Verse)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReader(readerEntity: QuranReader)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSurah(sura:Surah)


    @Query("select * from QuranReader")
    fun getAllReaders () : Flow<List<QuranReader>>

    @Query("select * from QuranReader where readerId =:id")
    fun getReader(id:Int) : Flow<QuranReader>

    @Query("select * from surah where id=:id")
    fun getSurahById(id:Int) : Flow<Surah>

    @Query("select * from surah")
    fun getSavedSurah() : Flow<List<Surah>>

    @Query("select * from juz")
    fun getSavedJuzs() : Flow<List<Juz>>

    @Query("select * from verse where chapterId =:id")
    fun getSaveVerses(id:Int) : Flow<List<Verse>>

    @Query("select * from tafsiraya where verseKey=:key ")
    fun getSavedTafsir(key:String) : Flow<List<TafsirAya>>


    @Query("select * from surah_fts where surah_fts MATCH :search")
    fun searchByAtSurah(search:String) : Flow<List<SurahFts>>

}