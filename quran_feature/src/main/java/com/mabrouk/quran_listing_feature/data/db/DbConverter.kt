package com.mabrouk.quran_listing_feature.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mabrouk.quran_listing_feature.domain.models.Media
import com.mabrouk.quran_listing_feature.domain.models.Translation
import com.mabrouk.quran_listing_feature.domain.models.Word

class DbConverter {
   @TypeConverter
   fun fromMapToJson(data:Map<String,String>?) : String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToMap(data:String?) : Map<String,String>? {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun fromTranslationListToJson(data:ArrayList<Translation>?) :String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToTranslationList(data: String?): ArrayList<Translation>? {
        val type= object : TypeToken<ArrayList<Translation>>(){}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun fromMediaListToJson(data:ArrayList<Media>?) :String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToMediaList(data: String?): ArrayList<Media>? {
        val type= object : TypeToken<ArrayList<Media>>(){}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun fromLonListToJson(data:ArrayList<Long>?) :String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToLongList(data: String?): ArrayList<Long>? {
        val type= object : TypeToken<ArrayList<Long>>(){}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun fromWordListToJson(data:ArrayList<Word>?) :String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToWordList(data: String?): ArrayList<Word>? {
        val type= object : TypeToken<ArrayList<Word>>(){}.type
        return Gson().fromJson(data,type)
    }

}