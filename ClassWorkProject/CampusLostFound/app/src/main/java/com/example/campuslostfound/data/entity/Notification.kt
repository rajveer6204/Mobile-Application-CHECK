package com.example.campuslostfound.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: String = "ALERT" // MATCH, MESSAGE, UPDATE, REMINDER
)
