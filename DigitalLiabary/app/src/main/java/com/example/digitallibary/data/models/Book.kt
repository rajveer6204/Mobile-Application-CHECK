package com.example.digitallibary.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String,
    val category: String,
    val description: String,
    val coverGradientIndex: Int = 0,
    val pdfFileName: String,
    val totalPages: Int = 10,
    val rating: Double = 4.5,
    val publishedYear: Int = 2023,
    val isFeatured: Boolean = false,
    val isPopular: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis()
)
