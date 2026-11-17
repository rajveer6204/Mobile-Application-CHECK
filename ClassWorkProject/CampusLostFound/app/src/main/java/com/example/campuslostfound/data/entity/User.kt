package com.example.campuslostfound.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val collegeId: String,
    val email: String,
    val password: String,
    val phoneNumber: String = "",
    val avatarUrl: String = ""
)
