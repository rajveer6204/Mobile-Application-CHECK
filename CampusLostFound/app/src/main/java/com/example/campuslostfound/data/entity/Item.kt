package com.example.campuslostfound.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ItemType {
    LOST,
    FOUND
}

enum class ItemStatus {
    ACTIVE,
    RESOLVED
}

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemName: String,
    val category: String,
    val description: String,
    val location: String,
    val imageUri: String = "",
    val itemType: ItemType,
    val userId: Long,
    val uploaderName: String = "Campus Student",
    val date: String,
    val status: ItemStatus = ItemStatus.ACTIVE,
    val contactInfo: String = ""
)
