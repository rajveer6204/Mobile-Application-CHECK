package com.example.digitallibary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.models.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark): Long

    @Query("DELETE FROM bookmarks WHERE userId = :userId AND bookId = :bookId")
    fun removeBookmark(userId: Long, bookId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE userId = :userId AND bookId = :bookId LIMIT 1)")
    fun isBookmarked(userId: Long, bookId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE userId = :userId AND bookId = :bookId LIMIT 1)")
    fun isBookmarkedSingle(userId: Long, bookId: Long): Boolean

    @Query("SELECT b.* FROM books b INNER JOIN bookmarks bm ON b.id = bm.bookId WHERE bm.userId = :userId ORDER BY bm.addedAt DESC")
    fun getBookmarkedBooks(userId: Long): Flow<List<Book>>
}
