package com.mabrouk.quran_listing_feature.presentation.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mabrouk.core.network.Result.*
import com.mabrouk.core.network.decimalFormat
import com.mabrouk.core.utils.FileUtils
import com.mabrouk.quran_listing_feature.BuildConfig
import com.mabrouk.quran_listing_feature.data.repository.AyaRepository
import com.mabrouk.quran_listing_feature.presentation.utils.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
@HiltWorker
class AudioDownloader @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    val repository: AyaRepository
) : CoroutineWorker(context, params) {

    var result:String?=null
    override suspend fun doWork(): Result {
        val data = Data.Builder()
        inputData.getString(SURA_LIST_AUDIOS)?.let {
//            if (!FileUtils.fileIsFound(READER_1,1,0)){
//                downloadAya(READER_1,1,0)
//            }

            fromToObject(it)?.apply {
                if (!FileUtils.fileIsFound(this.first().url,1,0)){
                    downloadAya(this.first().url,1,0)
                }
                forEach { item ->
                    result = if (FileUtils.fileIsFound(item.url,item.chapter_id,item.ayaNum)){
                        null
                    }else {
                        downloadAya(item.url, item.chapter_id, item.ayaNum)
                    }

                    if (repository.getSavedTafsir(item.chapter_id,item.ayaNum).firstOrNull().isNullOrEmpty()){
                        downloadTafsir(item.chapter_id,item.ayaNum,4)
                    }
                }
                data.putInt(LAST_ID,last().id)
            }
        }
        data.putString(AUDIO_DOWNLOAD, result)
        if (result==null)return Result.success(data.build())
        return Result.failure(data.build())
    }

    suspend fun downloadAya(url:String,sura:Int,aya:Int) : String?{
        var result:String? =null
        repository.downloadAudio("${BuildConfig.Audio_Url2}$url/${sura.decimalFormat()}${aya.decimalFormat()}.mp3").collect {
            when (it) {
                is OnSuccess -> {
                    //withContext(Dispatchers.IO){downloadTafsir(sura,aya,4)}
                    Log.d("save File", FileUtils.saveAudio(it.data,url, sura, aya).toString())
                }
                is OnFailure -> result = it.throwable.message!!
                is NoInternetConnect -> result = it.error
            }
        }
        return result
    }

    suspend fun downloadTafsir(chapter_id:Int,verse_id:Int,count:Int) : String?{
        var result:String? =null
        repository.requestTafsir(chapter_id, verse_id,count).collect{
            when(it){
                is OnSuccess -> {
                    withContext(Dispatchers.IO) {
                        repository.saveTafsir(it.data)
                    }
                    if (count>1){
                        downloadTafsir(chapter_id, verse_id, count-1)
                    }
                }
                is OnFailure -> result= it.throwable.message!!
                is NoInternetConnect -> result = it.error
            }
        }
        return result
    }

    fun fromToObject(json:String?) : ArrayList<AudioDataPass>?{
        val type = object : TypeToken<ArrayList<AudioDataPass>>(){}.type
        return Gson().fromJson(json,type)
    }

}