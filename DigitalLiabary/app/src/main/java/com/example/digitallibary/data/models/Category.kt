package com.example.digitallibary.data.models

data class Category(
    val id: String,
    val name: String,
    val iconName: String,
    val bookCount: Int = 0
)
