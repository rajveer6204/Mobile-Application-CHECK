package com.example.digitallibary.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_history")
data class ReadingHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val bookId: Long,
    val lastPageRead: Int = 1,
    val totalPages: Int = 1,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)
