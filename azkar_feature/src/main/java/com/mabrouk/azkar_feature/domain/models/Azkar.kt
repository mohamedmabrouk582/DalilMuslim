package com.mabrouk.azkar_feature.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@Entity(tableName = "azkar")
data class Azkar(
    @ColumnInfo(name = "category")
    val cetegory:String?,
    val zekr:String?,
    val description:String?,
    val count:Int?=1,
    val reference:String?,
    @PrimaryKey
    val id:Int?
)
