package com.mabrouk.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.gson.JsonSyntaxException
import com.mabrouk.core.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.net.ssl.SSLHandshakeException
import kotlin.collections.ArrayList

sealed class Result<out T : Any> {
    object OnLoading : Result<Nothing>()
    object OnFinish : Result<Nothing>()
    data class OnSuccess<out T : Any>(val data: T) : Result<T>()
    data class OnFailure(val throwable: Throwable) : Result<Nothing>()
    data class NoInternetConnect(val error: String) : Result<Nothing>()
}

typealias ApiResult<T> = suspend () -> Response<T>
typealias ApiResult2<T> = suspend () -> Deferred<T>

fun <T : Any> executeCall(
    context: Context,
    messageInCaseOfError: String = "Network error",
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    apiCall: ApiResult<T>
): Flow<Result<T>> {
    return flow {
        emit(Result.OnLoading)
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(Result.OnSuccess(it))
            } ?: emit(Result.OnFailure(IOException("API call successful but empty response body")))
            return@flow
        }

        emit(
            Result.OnFailure(
                IOException(
                    "API call failed with error - ${
                        response.errorBody()?.string() ?: messageInCaseOfError
                    }"
                )
            )
        )
        return@flow
    }.final(context, allowRetries, numberOfRetries)
}


fun <T : Any> executeCall2(
    context: Context,
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    apiCall: ApiResult2<T>
): Flow<Result<T>> {
    return flow {
        emit(Result.OnLoading)
        val response = apiCall()
        emit(Result.OnSuccess(response.await()))
        return@flow
    }.final(context, allowRetries, numberOfRetries)
}

fun <T : Any> Flow<Result<T>>.final(
    context: Context,
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
) : Flow<Result<T>>{
    var delayDuration = 1000L
    val delayFactor = 2
   return this.catch { e ->
        if (CheckNetwork.isOnline(context)) {
            emit(Result.OnFailure(IOException(errorMsg(e, context))))
        } else {
            emit(Result.NoInternetConnect(context.getString(R.string.no_internet_connection)))
        }
        return@catch
    }.retryWhen { cause, attempt ->
        if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
        delay(delayDuration)
        delayDuration *= delayFactor
        return@retryWhen true
    }.onCompletion {
        emit(Result.OnFinish)
    }.flowOn(Dispatchers.IO)
}



interface OnCheckConnection {
    fun ConnectionTrue()
    fun ConnectionError()
}

class CheckNetwork {
    companion object {

        fun isConnected(context: Context, onCheckConnection: OnCheckConnection) {
            if (isOnline(context)) {
                onCheckConnection.ConnectionTrue()
            } else {
                onCheckConnection.ConnectionError()
            }
        }

        @SuppressLint("NewApi")
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }
    }
}

fun errorMsg(e: Throwable, context: Context): String =
    when (e) {
        is HttpException -> analysisError(e)
        is SocketTimeoutException -> context.getString(R.string.socketTimeout)
        is JsonSyntaxException -> context.getString(R.string.Jsonpars)
        is SSLHandshakeException -> e.message.toString()
        else -> "Api Error"
    }


private fun analysisError(e: HttpException): String {
    return try {
        val responseStrings: String = e.response()!!.errorBody().toString()
        val jsonObject = JSONObject(responseStrings)
        when {
            jsonObject.has("msg") -> jsonObject.get("msg").toString()
            jsonObject.has("error") -> jsonObject.get("error").toString()
            else -> e.message()
        }
    } catch (ex: Exception) {
        (if (e.message().isEmpty()) e.localizedMessage else e.message()) as String
    }
}

fun <T : Any> List<T>.toArrayList() = ArrayList(this)

fun Int.decimalFormat(): String {
    val pattern = "000"
    val number = NumberFormat.getNumberInstance(Locale.US)
    val myFormatter = number as DecimalFormat
    myFormatter.applyPattern(pattern)
    return myFormatter.format(this)
}


fun Context.loader(
    stream: SharedFlow<String> = MutableSharedFlow(),
    scope: CoroutineScope
): AlertDialog {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.WrapContentDialog)
    val view = LayoutInflater.from(this).inflate(R.layout.loader_layout, null, false)
    val textView = view.findViewById<TextView>(R.id.file_txt)
    scope.launch {
        stream.collectLatest {
            textView.text = it
        }
    }
    builder.setView(view)
    return builder.create().apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false)
    }
}

fun String.Normalize():String{
    var input = this
    //Remove honorific sign
    input=input.replace("\u0610", "");//ARABIC SIGN SALLALLAHOU ALAYHE WA SALLAM
    input=input.replace("\u0611", "");//ARABIC SIGN ALAYHE ASSALLAM
    input=input.replace("\u0612", "");//ARABIC SIGN RAHMATULLAH ALAYHE
    input=input.replace("\u0613", "");//ARABIC SIGN RADI ALLAHOU ANHU
    input=input.replace("\u0614", "");//ARABIC SIGN TAKHALLUS

    //Remove koranic anotation
    input=input.replace("\u0615", "");//ARABIC SMALL HIGH TAH
    input=input.replace("\u0616", "");//ARABIC SMALL HIGH LIGATURE ALEF WITH LAM WITH YEH
    input=input.replace("\u0617", "");//ARABIC SMALL HIGH ZAIN
    input=input.replace("\u0618", "");//ARABIC SMALL FATHA
    input=input.replace("\u0619", "");//ARABIC SMALL DAMMA
    input=input.replace("\u061A", "");//ARABIC SMALL KASRA
    input=input.replace("\u06D6", "");//ARABIC SMALL HIGH LIGATURE SAD WITH LAM WITH ALEF MAKSURA
    input=input.replace("\u06D7", "");//ARABIC SMALL HIGH LIGATURE QAF WITH LAM WITH ALEF MAKSURA
    input=input.replace("\u06D8", "");//ARABIC SMALL HIGH MEEM INITIAL FORM
    input=input.replace("\u06D9", "");//ARABIC SMALL HIGH LAM ALEF
    input=input.replace("\u06DA", "");//ARABIC SMALL HIGH JEEM
    input=input.replace("\u06DB", "");//ARABIC SMALL HIGH THREE DOTS
    input=input.replace("\u06DC", "");//ARABIC SMALL HIGH SEEN
    input=input.replace("\u06DD", "");//ARABIC END OF AYAH
    input=input.replace("\u06DE", "");//ARABIC START OF RUB EL HIZB
    input=input.replace("\u06DF", "");//ARABIC SMALL HIGH ROUNDED ZERO
    input=input.replace("\u06E0", "");//ARABIC SMALL HIGH UPRIGHT RECTANGULAR ZERO
    input=input.replace("\u06E1", "");//ARABIC SMALL HIGH DOTLESS HEAD OF KHAH
    input=input.replace("\u06E2", "");//ARABIC SMALL HIGH MEEM ISOLATED FORM
    input=input.replace("\u06E3", "");//ARABIC SMALL LOW SEEN
    input=input.replace("\u06E4", "");//ARABIC SMALL HIGH MADDA
    input=input.replace("\u06E5", "");//ARABIC SMALL WAW
    input=input.replace("\u06E6", "");//ARABIC SMALL YEH
    input=input.replace("\u06E7", "");//ARABIC SMALL HIGH YEH
    input=input.replace("\u06E8", "");//ARABIC SMALL HIGH NOON
    input=input.replace("\u06E9", "");//ARABIC PLACE OF SAJDAH
    input=input.replace("\u06EA", "");//ARABIC EMPTY CENTRE LOW STOP
    input=input.replace("\u06EB", "");//ARABIC EMPTY CENTRE HIGH STOP
    input=input.replace("\u06EC", "");//ARABIC ROUNDED HIGH STOP WITH FILLED CENTRE
    input=input.replace("\u06ED", "");//ARABIC SMALL LOW MEEM

    //Remove tatweel
    input=input.replace("\u0640", "");

    //Remove tashkeel
    input=input.replace("\u064B", "");//ARABIC FATHATAN
    input=input.replace("\u064C", "");//ARABIC DAMMATAN
    input=input.replace("\u064D", "");//ARABIC KASRATAN
    input=input.replace("\u064E", "");//ARABIC FATHA
    input=input.replace("\u064F", "");//ARABIC DAMMA
    input=input.replace("\u0650", "");//ARABIC KASRA
    input=input.replace("\u0651", "");//ARABIC SHADDA
    input=input.replace("\u0652", "");//ARABIC SUKUN
    input=input.replace("\u0653", "");//ARABIC MADDAH ABOVE
    input=input.replace("\u0654", "");//ARABIC HAMZA ABOVE
    input=input.replace("\u0655", "");//ARABIC HAMZA BELOW
    input=input.replace("\u0656", "");//ARABIC SUBSCRIPT ALEF
    input=input.replace("\u0657", "");//ARABIC INVERTED DAMMA
    input=input.replace("\u0658", "");//ARABIC MARK NOON GHUNNA
    input=input.replace("\u0659", "");//ARABIC ZWARAKAY
    input=input.replace("\u065A", "");//ARABIC VOWEL SIGN SMALL V ABOVE
    input=input.replace("\u065B", "");//ARABIC VOWEL SIGN INVERTED SMALL V ABOVE
    input=input.replace("\u065C", "");//ARABIC VOWEL SIGN DOT BELOW
    input=input.replace("\u065D", "");//ARABIC REVERSED DAMMA
    input=input.replace("\u065E", "");//ARABIC FATHA WITH TWO DOTS
    input=input.replace("\u065F", "");//ARABIC WAVY HAMZA BELOW
    input=input.replace("\u0670", "");//ARABIC LETTER SUPERSCRIPT ALEF

    //Replace Waw Hamza Above by Waw
    input=input.replace("\u0624", "\u0648");

    //Replace Ta Marbuta by Ha
    input=input.replace("\u0629", "\u0647");

    //Replace Ya
    // and Ya Hamza Above by Alif Maksura
    input=input.replace("\u064A", "\u0649");
    input=input.replace("\u0626", "\u0649");

    // Replace Alifs with Hamza Above/Below
    // and with Madda Above by Alif
    input=input.replace("\u0622", "\u0627");
    input=input.replace("\u0623", "\u0627");
    input=input.replace("\u0625", "\u0627");
    return input
}

