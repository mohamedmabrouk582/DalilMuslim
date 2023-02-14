package com.example.prayertime.domian.alram

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 2/9/23
 */
interface AlarmScheduler {
    fun alarmSchedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}