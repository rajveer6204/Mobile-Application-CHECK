package com.example.digitallibary.data.repository

import com.example.digitallibary.data.database.BookDao
import com.example.digitallibary.data.database.BookmarkDao
import com.example.digitallibary.data.database.ReadingHistoryDao
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.models.BookWithProgress
import com.example.digitallibary.data.models.Bookmark
import com.example.digitallibary.data.models.ReadingHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class BookRepository(
    private val bookDao: BookDao,
    private val bookmarkDao: BookmarkDao,
    private val readingHistoryDao: ReadingHistoryDao
) {
    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    fun getFeaturedBooks(): Flow<List<Book>> = bookDao.getFeaturedBooks()

    fun getPopularBooks(): Flow<List<Book>> = bookDao.getPopularBooks()

    fun getBookById(bookId: Long): Flow<Book?> = bookDao.getBookById(bookId)

    suspend fun getBookByIdSingle(bookId: Long): Book? = withContext(Dispatchers.IO) {
        bookDao.getBookByIdSingle(bookId)
    }

    fun getBooksByCategory(category: String): Flow<List<Book>> {
        return if (category == "All") {
            bookDao.getAllBooks()
        } else {
            bookDao.getBooksByCategory(category)
        }
    }

    fun searchBooks(query: String): Flow<List<Book>> = bookDao.searchBooks(query)

    fun isBookmarked(userId: Long, bookId: Long): Flow<Boolean> =
        bookmarkDao.isBookmarked(userId, bookId)

    suspend fun toggleBookmark(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        val currentlyBookmarked = bookmarkDao.isBookmarkedSingle(userId, bookId)
        if (currentlyBookmarked) {
            bookmarkDao.removeBookmark(userId, bookId)
            false
        } else {
            bookmarkDao.insertBookmark(Bookmark(userId = userId, bookId = bookId))
            true
        }
    }

    fun getBookmarkedBooks(userId: Long): Flow<List<Book>> =
        bookmarkDao.getBookmarkedBooks(userId)

    suspend fun updateReadingProgress(userId: Long, bookId: Long, pageRead: Int, totalPages: Int) = withContext(Dispatchers.IO) {
        val existing = readingHistoryDao.getHistoryForBook(userId, bookId)
        val isCompleted = pageRead >= totalPages
        val updatedHistory = existing?.copy(
            lastPageRead = pageRead,
            totalPages = totalPages,
            lastReadTimestamp = System.currentTimeMillis(),
            isCompleted = isCompleted
        ) ?: ReadingHistory(
            userId = userId,
            bookId = bookId,
            lastPageRead = pageRead,
            totalPages = totalPages,
            lastReadTimestamp = System.currentTimeMillis(),
            isCompleted = isCompleted
        )
        readingHistoryDao.insertOrUpdateHistory(updatedHistory)
    }

    fun getHistoryForBook(userId: Long, bookId: Long): Flow<ReadingHistory?> =
        readingHistoryDao.getHistoryForBookFlow(userId, bookId)

    fun getUserReadingHistoryWithDetails(userId: Long): Flow<List<BookWithProgress>> {
        return combine(
            readingHistoryDao.getUserReadingHistory(userId),
            bookDao.getAllBooks()
        ) { historyList, allBooks ->
            val bookMap = allBooks.associateBy { it.id }
            historyList.mapNotNull { history ->
                val book = bookMap[history.bookId] ?: return@mapNotNull null
                BookWithProgress(
                    book = book,
                    isBookmarked = false,
                    lastPageRead = history.lastPageRead,
                    totalPages = history.totalPages,
                    lastReadTimestamp = history.lastReadTimestamp
                )
            }
        }
    }
}
