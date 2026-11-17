package com.example.campuslostfound.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderId: Long,
    val receiverId: Long,
    val itemId: Long,
    val itemName: String = "Campus Item",
    val otherUserName: String = "Student",
    val message: String,
    val timestamp: String,
    val isFromUser: Boolean = true
)
