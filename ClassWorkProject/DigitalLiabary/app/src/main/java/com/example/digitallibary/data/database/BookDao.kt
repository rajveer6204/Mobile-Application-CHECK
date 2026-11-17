package com.example.digitallibary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.digitallibary.data.models.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(books: List<Book>)

    @Query("SELECT * FROM books ORDER BY dateAdded DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isFeatured = 1 ORDER BY rating DESC")
    fun getFeaturedBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isPopular = 1 ORDER BY rating DESC")
    fun getPopularBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
    fun getBookById(bookId: Long): Flow<Book?>

    @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
    fun getBookByIdSingle(bookId: Long): Book?

    @Query("SELECT * FROM books WHERE category = :category ORDER BY rating DESC")
    fun getBooksByCategory(category: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<Book>>

    @Query("SELECT COUNT(*) FROM books")
    fun getBookCount(): Int
}
