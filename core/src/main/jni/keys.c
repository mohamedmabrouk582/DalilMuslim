#include <jni.h>JNIEXPORT jstring JNICALL


JNIEXPORT jstring JNICALL
Java_com_mabrouk_core_network_KeysKt_getApiKey(JNIEnv *env, jclass clazz) {
 return (*env)-> NewStringUTF(env,"SqD712P3E82xnwOAEOkGd5JZH8s9wRR24TqNFzjk");
}

JNIEXPORT jstring JNICALL
Java_com_mabrouk_core_network_KeysKt_getBaseUrl(JNIEnv *env, jclass clazz) {
 return (*env)-> NewStringUTF(env,"http://api.quran.com/api/v3/");
}

JNIEXPORT jstring JNICALL
Java_com_mabrouk_core_network_KeysKt_getBaseUrlSunnah(JNIEnv *env, jclass clazz) {
 return (*env)-> NewStringUTF(env,"https://api.sunnah.com/v1/");
}

JNIEXPORT jstring JNICALL
Java_com_mabrouk_core_network_KeysKt_getBaseUrlTafseer(JNIEnv *env, jclass clazz) {
 return (*env)-> NewStringUTF(env,"http://api.quran-tafseer.com/");
}

JNIEXPORT jstring JNICALL
Java_com_mabrouk_core_network_KeysKt_getAudioUrl(JNIEnv *env, jclass clazz) {
 return (*env)-> NewStringUTF(env,"http://www.everyayah.com/data/");
}

JNIEXPORT jstring JNICALL
Java_com_mabrouk_core_network_KeysKt_getAudioUrl2(JNIEnv *env, jclass clazz) {
 return (*env)-> NewStringUTF(env,"http://verse.mp3quran.net/arabic/");
}