package com.mabrouk.hadith_feature.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mabrouk.hadith_feature.domain.models.Hadith
import com.mabrouk.hadith_feature.domain.models.HadithBook
import com.mabrouk.hadith_feature.domain.models.HadithCollection

class DataConverter {

    @TypeConverter
    fun fromHadithCollectToJson(data:ArrayList<HadithCollection>?) : String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToHadithCollection(data:String?) : ArrayList<HadithCollection>? {
        val type = object : TypeToken<ArrayList<HadithCollection>>() {}.type
        return Gson().fromJson(data,type)
    }


    @TypeConverter
    fun fromHadithBookToJson(data:ArrayList<HadithBook>?) : String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToHadithBook(data:String?) : ArrayList<HadithBook>? {
        val type = object : TypeToken<ArrayList<HadithBook>>() {}.type
        return Gson().fromJson(data,type)
    }


    @TypeConverter
    fun fromHadithToJson(data:ArrayList<Hadith>?) : String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToHadith(data:String?) : ArrayList<Hadith>? {
        val type = object : TypeToken<ArrayList<Hadith>>() {}.type
        return Gson().fromJson(data,type)
    }


}