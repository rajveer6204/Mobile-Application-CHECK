package com.example.digitallibary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.digitallibary.data.models.ReadingHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateHistory(history: ReadingHistory): Long

    @Query("SELECT * FROM reading_history WHERE userId = :userId AND bookId = :bookId LIMIT 1")
    fun getHistoryForBook(userId: Long, bookId: Long): ReadingHistory?

    @Query("SELECT * FROM reading_history WHERE userId = :userId AND bookId = :bookId LIMIT 1")
    fun getHistoryForBookFlow(userId: Long, bookId: Long): Flow<ReadingHistory?>

    @Query("SELECT * FROM reading_history WHERE userId = :userId ORDER BY lastReadTimestamp DESC")
    fun getUserReadingHistory(userId: Long): Flow<List<ReadingHistory>>
}
