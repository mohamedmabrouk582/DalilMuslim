package com.mabrouk.core.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import com.mabrouk.core.utils.FileUtils.isFileBathFound
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream


object FileUtils {

    fun saveMp3(
        context: Context,
        bytes: ResponseBody,
        url: String,
        suraNum: Int,
        ayaNum: Int
    ): Boolean {
        val internalStorageDir: File = context.filesDir

        return try {
            val file = File(internalStorageDir, "$url/$suraNum/$suraNum$ayaNum.mp3")
            if (file.parentFile?.exists() == null || file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            val outputStream = FileOutputStream(file)
            outputStream.write(bytes.bytes())
            outputStream.close()

            MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getMp3Path(context: Context, name: String): String {
        val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val file = File(externalFilesDir, "$name.mp3")
        return file.absolutePath
    }

    fun String.isFileBathFound(context: Context) :Boolean {
        val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val downloadedFile = File(externalFilesDir, "$this.mp3")
        return downloadedFile.exists()
    }


    fun getMp3Path(
        context: Context,
        url: String,
        suraNum: Int,
        ayaNum: Int
    ): String {
        val internalStorageDir: File = context.filesDir
        val file = File(internalStorageDir, "$url/$suraNum/$suraNum$ayaNum.mp3")
        return file.absolutePath
    }


    fun saveAudio(bytes: ResponseBody, url: String, suraNum: Int, ayaNum: Int): Boolean {
        //val root = if (hasSdCard()){
//         val root=  Environment.getExternalStorageState()
        //}
//      else{
//           Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
//       }
        val root = Environment.getExternalStorageDirectory().toString()
        val dir = File("$root/quran_audios/$url/$suraNum")
        if (!dir.exists()) dir.mkdirs()
        val fileName = "$suraNum$ayaNum.mp3"
        val file = File(dir, fileName)
        if (file.exists()) return true
        return try {
            val out = FileOutputStream(file)
            out.write(bytes.bytes())
            out.flush()
            out.close()
            //out.write(bytes)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun saveVideo(bytes: ResponseBody, title: String, ext: String): Boolean {
        //val root = if (hasSdCard()){
//         val root=  Environment.getExternalStorageState()
        //}
//      else{
//           Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
//       }
        val root = Environment.getExternalStorageDirectory().toString()
        val dir = File("$root/quran_videos")
        if (!dir.exists()) dir.mkdirs()
        val fileName = "$title.$ext"
        val file = File(dir, fileName)
        if (file.exists()) return true
        return try {
            val out = FileOutputStream(file)
            out.write(bytes.bytes())
            out.flush()
            out.close()
            //out.write(bytes)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun fileIsFound(context: Context, url: String, suraNum: Int, ayaNum: Int) =
        File(getMp3Path(context, url, suraNum, ayaNum)).exists()

    fun videoIsFound(title: String, ext: String) = File(getVideoPath(title, ext)).exists()

    fun getAudioPath(url: String, suraNum: Int, ayaNum: Int): String {
//        val root = if (hasSdCard()){
//            Environment.getExternalStorageState()
//        }else{
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
//        }
        val root = Environment.getExternalStorageDirectory().toString()
        val dir = File("$root/quran_audios/$url/$suraNum")
        val fileName = "$suraNum$ayaNum.mp3"
        val file = File(dir, fileName)
        return file.absolutePath
    }

    fun getVideoPath(title: String, ext: String): String {
        val root = Environment.getExternalStorageDirectory().toString()
        val dir = File("$root/quran_videos")
        val fileName = "$title.$ext"
        val file = File(dir, fileName)
        return file.absolutePath
    }


    private fun hasSdCard(): Boolean {
        val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        val isSDSupportedDevice = Environment.isExternalStorageRemovable()
        return isSDSupportedDevice && isSDPresent
    }

}