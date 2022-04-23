package com.mabrouk.history_feature.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mabrouk.history_feature.domain.models.Story
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/19/22
 */
@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(storyEntity: Story) : Long

    @Query("select * from Story")
    fun getStories() : Flow<List<Story>>

    @Query("select * from Story where title LIKE '%' || :query || '%'")
    fun search(query:String) : Flow<Story>
}