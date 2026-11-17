package com.example.campuslostfound.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.campuslostfound.data.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message): Long

    @Query("SELECT * FROM messages WHERE itemId = :itemId ORDER BY id ASC")
    fun getMessagesForItem(itemId: Long): Flow<List<Message>>

    @Query("SELECT * FROM messages GROUP BY itemId ORDER BY id DESC")
    fun getConversations(): Flow<List<Message>>
}
