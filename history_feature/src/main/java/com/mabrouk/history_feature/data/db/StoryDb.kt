package com.mabrouk.history_feature.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mabrouk.history_feature.domain.models.Story

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@Database(entities = [Story::class], version = 1 , exportSchema = true)
abstract class StoryDb : RoomDatabase(){
    abstract fun getDao() : StoryDao
}