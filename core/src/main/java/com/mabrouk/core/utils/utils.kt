package com.mabrouk.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */

fun getCurrentDate(pattern:String = "dd-MM-yyyy"): String{
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(LocalDateTime.now())
}

fun getDate(date:LocalDateTime,pattern:String = "dd-MM-yyyy"): String{
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(date)
}