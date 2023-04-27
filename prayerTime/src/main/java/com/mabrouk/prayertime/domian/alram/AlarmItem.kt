package com.mabrouk.prayertime.domian.alram

import java.time.LocalDateTime

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */
data class AlarmItem(
    val time: LocalDateTime,
    val massage: String,
    val tosheh: Boolean = false,
    val TwashehFajar: Boolean = false
)
