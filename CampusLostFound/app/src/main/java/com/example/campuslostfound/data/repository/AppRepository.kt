package com.example.campuslostfound.data.repository

import com.example.campuslostfound.data.database.AppDatabase
import com.example.campuslostfound.data.entity.Item
import com.example.campuslostfound.data.entity.ItemStatus
import com.example.campuslostfound.data.entity.ItemType
import com.example.campuslostfound.data.entity.Message
import com.example.campuslostfound.data.entity.Notification
import com.example.campuslostfound.data.entity.User
import kotlinx.coroutines.flow.Flow

class AppRepository(private val db: AppDatabase) {

    // User Operations
    suspend fun registerUser(user: User): Long = db.userDao().insertUser(user)
    suspend fun updateUser(user: User) = db.userDao().updateUser(user)
    suspend fun login(email: String, pass: String): User? = db.userDao().login(email, pass)
    suspend fun getUserById(userId: Long): User? = db.userDao().getUserById(userId)
    fun getUserByIdFlow(userId: Long): Flow<User?> = db.userDao().getUserByIdFlow(userId)
    suspend fun getUserByEmail(email: String): User? = db.userDao().getUserByEmail(email)

    // Item Operations
    suspend fun insertItem(item: Item): Long = db.itemDao().insertItem(item)
    suspend fun updateItem(item: Item) = db.itemDao().updateItem(item)
    suspend fun deleteItem(item: Item) = db.itemDao().deleteItem(item)
    fun getAllItems(): Flow<List<Item>> = db.itemDao().getAllItems()
    fun getItemsByType(type: ItemType): Flow<List<Item>> = db.itemDao().getItemsByType(type)
    fun getItemsByUserAndType(userId: Long, type: ItemType): Flow<List<Item>> =
        db.itemDao().getItemsByUserAndType(userId, type)
    fun getItemsByUser(userId: Long): Flow<List<Item>> = db.itemDao().getItemsByUser(userId)
    suspend fun getItemById(itemId: Long): Item? = db.itemDao().getItemById(itemId)
    fun getItemByIdFlow(itemId: Long): Flow<Item?> = db.itemDao().getItemByIdFlow(itemId)
    fun searchItems(query: String): Flow<List<Item>> = db.itemDao().searchItems(query)
    suspend fun updateItemStatus(itemId: Long, status: ItemStatus) =
        db.itemDao().updateStatus(itemId, status)

    // Message Operations
    suspend fun insertMessage(message: Message): Long = db.messageDao().insertMessage(message)
    fun getMessagesForItem(itemId: Long): Flow<List<Message>> =
        db.messageDao().getMessagesForItem(itemId)
    fun getConversations(): Flow<List<Message>> = db.messageDao().getConversations()

    // Notification Operations
    suspend fun insertNotification(notification: Notification): Long =
        db.notificationDao().insertNotification(notification)
    fun getAllNotifications(): Flow<List<Notification>> =
        db.notificationDao().getAllNotifications()
    suspend fun markNotificationAsRead(id: Long) = db.notificationDao().markAsRead(id)
    suspend fun markAllNotificationsAsRead() = db.notificationDao().markAllAsRead()
    suspend fun clearAllNotifications() = db.notificationDao().clearAll()
}
