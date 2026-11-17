package com.example.digitallibary.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val bookId: Long,
    val addedAt: Long = System.currentTimeMillis()
)
