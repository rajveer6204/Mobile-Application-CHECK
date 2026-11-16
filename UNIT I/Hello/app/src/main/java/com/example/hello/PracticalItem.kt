package com.example.hello

data class PracticalItem(
    val id: Int,
    val title: String,
    val iconResId: Int,
    val targetActivity: Class<*>
)
