package com.mabrouk.prayertime.domian.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CallingApi(
    @PrimaryKey
    val timing: String
)