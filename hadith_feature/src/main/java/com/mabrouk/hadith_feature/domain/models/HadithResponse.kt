package com.mabrouk.hadith_feature.domain.models

data class HadithResponse<T:Any>(
    val data:T,
    val total:Int,
    val limit:Int,
    val previous:Int?,
    val next:Int?
)