package com.example.campuslostfound.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.campuslostfound.data.entity.Item
import com.example.campuslostfound.data.entity.ItemStatus
import com.example.campuslostfound.data.entity.ItemType
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item): Long

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM items ORDER BY id DESC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE itemType = :type ORDER BY id DESC")
    fun getItemsByType(type: ItemType): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE userId = :userId AND itemType = :type ORDER BY id DESC")
    fun getItemsByUserAndType(userId: Long, type: ItemType): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE userId = :userId ORDER BY id DESC")
    fun getItemsByUser(userId: Long): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :itemId LIMIT 1")
    suspend fun getItemById(itemId: Long): Item?

    @Query("SELECT * FROM items WHERE id = :itemId LIMIT 1")
    fun getItemByIdFlow(itemId: Long): Flow<Item?>

    @Query("SELECT * FROM items WHERE itemName LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchItems(query: String): Flow<List<Item>>

    @Query("UPDATE items SET status = :status WHERE id = :itemId")
    suspend fun updateStatus(itemId: Long, status: ItemStatus)
}
