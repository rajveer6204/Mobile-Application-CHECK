package com.example.campuslostfound.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.campuslostfound.data.dao.ItemDao
import com.example.campuslostfound.data.dao.MessageDao
import com.example.campuslostfound.data.dao.NotificationDao
import com.example.campuslostfound.data.dao.UserDao
import com.example.campuslostfound.data.entity.Item
import com.example.campuslostfound.data.entity.ItemStatus
import com.example.campuslostfound.data.entity.ItemType
import com.example.campuslostfound.data.entity.Message
import com.example.campuslostfound.data.entity.Notification
import com.example.campuslostfound.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Item::class, Message::class, Notification::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun itemDao(): ItemDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "campus_lost_found_db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            // Seed Default User
            val defaultUserId = db.userDao().insertUser(
                User(
                    id = 1,
                    fullName = "Alex Morgan",
                    collegeId = "KMRU202488",
                    email = "student@campus.edu",
                    password = "password123",
                    phoneNumber = "+1 (555) 234-5678",
                    avatarUrl = ""
                )
            )

            // Seed Lost & Found Items
            val sampleItems = listOf(
                Item(
                    id = 1,
                    itemName = "Apple Watch Series 8",
                    category = "Electronics",
                    description = "Midnight Blue Aluminum Case with Sport Band. Left near Computer Lab 3.",
                    location = "Central Library, 2nd Floor",
                    imageUri = "https://images.unsplash.com/photo-1546868871-7041f2a55e12",
                    itemType = ItemType.LOST,
                    userId = defaultUserId,
                    uploaderName = "Alex Morgan",
                    date = "Oct 24, 2024",
                    status = ItemStatus.ACTIVE,
                    contactInfo = "alex.morgan@campus.edu"
                ),
                Item(
                    id = 2,
                    itemName = "Leather College Backpack",
                    category = "Bags",
                    description = "Brown genuine leather backpack containing CS textbooks and a blue notebook.",
                    location = "Student Cafeteria Block B",
                    imageUri = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62",
                    itemType = ItemType.FOUND,
                    userId = 2,
                    uploaderName = "Sarah Jenkins",
                    date = "Oct 23, 2024",
                    status = ItemStatus.ACTIVE,
                    contactInfo = "sarah.j@campus.edu"
                ),
                Item(
                    id = 3,
                    itemName = "Car & Hostel Key Chain",
                    category = "Keys",
                    description = "Silver key ring with 3 keys and a blue metallic Toyota fob.",
                    location = "Sports Complex Pavilion",
                    imageUri = "https://images.unsplash.com/photo-1582139329536-e7284fece509",
                    itemType = ItemType.LOST,
                    userId = defaultUserId,
                    uploaderName = "Alex Morgan",
                    date = "Oct 22, 2024",
                    status = ItemStatus.ACTIVE,
                    contactInfo = "alex.morgan@campus.edu"
                ),
                Item(
                    id = 4,
                    itemName = "Sony WH-1000XM4 Headphones",
                    category = "Electronics",
                    description = "Black noise canceling over-ear headphones in protective hard case.",
                    location = "Auditorium Main Entrance",
                    imageUri = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e",
                    itemType = ItemType.FOUND,
                    userId = 3,
                    uploaderName = "David Chen",
                    date = "Oct 21, 2024",
                    status = ItemStatus.ACTIVE,
                    contactInfo = "david.c@campus.edu"
                ),
                Item(
                    id = 5,
                    itemName = "Student ID Card & Pass",
                    category = "Documents",
                    description = "Student ID card under name 'Michael Chang', Department of Engineering.",
                    location = "Science Block Gate 1",
                    imageUri = "",
                    itemType = ItemType.FOUND,
                    userId = 4,
                    uploaderName = "Campus Security Office",
                    date = "Oct 20, 2024",
                    status = ItemStatus.ACTIVE,
                    contactInfo = "security@campus.edu"
                ),
                Item(
                    id = 6,
                    itemName = "Calculus & Linear Algebra Book",
                    category = "Books",
                    description = "Hardcover 9th Edition textbook with yellow sticky notes inside.",
                    location = "Math Lecture Hall 102",
                    imageUri = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c",
                    itemType = ItemType.LOST,
                    userId = defaultUserId,
                    uploaderName = "Alex Morgan",
                    date = "Oct 19, 2024",
                    status = ItemStatus.RESOLVED,
                    contactInfo = "alex.morgan@campus.edu"
                )
            )

            sampleItems.forEach { db.itemDao().insertItem(it) }

            // Seed Sample Messages
            val sampleMessages = listOf(
                Message(
                    id = 1,
                    senderId = 2,
                    receiverId = defaultUserId,
                    itemId = 2,
                    itemName = "Leather College Backpack",
                    otherUserName = "Sarah Jenkins",
                    message = "Hi Alex! I found a brown backpack near the Cafeteria. Does it belong to you?",
                    timestamp = "10:30 AM",
                    isFromUser = false
                ),
                Message(
                    id = 2,
                    senderId = defaultUserId,
                    receiverId = 2,
                    itemId = 2,
                    itemName = "Leather College Backpack",
                    otherUserName = "Sarah Jenkins",
                    message = "Yes! That is mine. It has my CS notebook inside. Where can I meet you?",
                    timestamp = "10:32 AM",
                    isFromUser = true
                ),
                Message(
                    id = 3,
                    senderId = 3,
                    receiverId = defaultUserId,
                    itemId = 4,
                    itemName = "Sony WH-1000XM4 Headphones",
                    otherUserName = "David Chen",
                    message = "Hey! Is your missing item post still active?",
                    timestamp = "Yesterday",
                    isFromUser = false
                )
            )
            sampleMessages.forEach { db.messageDao().insertMessage(it) }

            // Seed Sample Notifications
            val sampleNotifications = listOf(
                Notification(
                    id = 1,
                    title = "Potential Match Found!",
                    description = "A new item matching 'Apple Watch' was recently reported near Library Block.",
                    timestamp = "10 mins ago",
                    isRead = false,
                    type = "MATCH"
                ),
                Notification(
                    id = 2,
                    title = "New Message Received",
                    description = "Sarah Jenkins sent you a message regarding Leather College Backpack.",
                    timestamp = "1 hour ago",
                    isRead = false,
                    type = "MESSAGE"
                ),
                Notification(
                    id = 3,
                    title = "Post Status Update",
                    description = "Your lost report 'Calculus Book' has been marked as RESOLVED.",
                    timestamp = "2 days ago",
                    isRead = true,
                    type = "UPDATE"
                ),
                Notification(
                    id = 4,
                    title = "Campus Lost & Found Safety Tip",
                    description = "Always verify item proof of ownership when meeting up on campus.",
                    timestamp = "3 days ago",
                    isRead = true,
                    type = "REMINDER"
                )
            )
            sampleNotifications.forEach { db.notificationDao().insertNotification(it) }
        }
    }
}
