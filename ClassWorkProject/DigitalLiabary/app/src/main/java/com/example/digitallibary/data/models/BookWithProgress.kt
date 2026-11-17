package com.example.digitallibary.data.models

data class BookWithProgress(
    val book: Book,
    val isBookmarked: Boolean = false,
    val lastPageRead: Int = 0,
    val totalPages: Int = book.totalPages,
    val progressPercentage: Int = if (book.totalPages > 0) ((lastPageRead.toFloat() / book.totalPages.toFloat()) * 100).toInt() else 0,
    val lastReadTimestamp: Long = 0L
)
