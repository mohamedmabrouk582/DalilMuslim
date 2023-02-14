package com.example.prayertime.data.db

import androidx.room.TypeConverter
import com.example.prayertime.domian.models.PrayerDate
import com.example.prayertime.domian.models.PrayerDateData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */
class PrayerConverter {

    @TypeConverter
    fun converttO(data:HashMap<String,String>?) : String? =
        Gson().toJson(data)

    @TypeConverter
    fun convertFrom(data:String?) : HashMap<String,String>? {
        val type = object : TypeToken<HashMap<String,String>>(){}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun convertPrayerDateData(data: PrayerDateData?) : String? = Gson().toJson(data)

    @TypeConverter
    fun convertToPrayerDateData(data: String?) : PrayerDateData?{
        val type = object : TypeToken<PrayerDateData>(){}.type
        return Gson().fromJson(data,type)
    }
}