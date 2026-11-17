package com.example.digitallibary.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.models.Bookmark
import com.example.digitallibary.data.models.ReadingHistory
import com.example.digitallibary.data.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Book::class, Bookmark::class, ReadingHistory::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun readingHistoryDao(): ReadingHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "digital_library_db"
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        private fun populateInitialData(database: AppDatabase) {
            val userDao = database.userDao()
            val bookDao = database.bookDao()

            // Seed default user
            if (userDao.getUserByEmail("badal@gmail.com") == null) {
                userDao.insertUser(
                    User(
                        id = 1,
                        fullName = "Badal Prasad",
                        email = "badal@gmail.com",
                        passwordHash = "password123",
                        bio = "Student & Passionate Reader"
                    )
                )
            }

            // Seed rich sample books
            val sampleBooks = listOf(
                Book(
                    id = 1,
                    title = "Atomic Habits",
                    author = "James Clear",
                    category = "Self Help",
                    description = "An easy and proven way to build good habits and break bad ones. Atomic Habits will reshape the way you think about progress and success, and give you the tools and strategies you need to transform your habits.",
                    coverGradientIndex = 0,
                    pdfFileName = "atomic_habits.pdf",
                    totalPages = 12,
                    rating = 4.8,
                    publishedYear = 2018,
                    isFeatured = true,
                    isPopular = true
                ),
                Book(
                    id = 2,
                    title = "Clean Code",
                    author = "Robert C. Martin",
                    category = "Technology",
                    description = "A Handbook of Agile Software Craftsmanship. Even bad code can function. But if code isn't clean, it can bring a development organization to its knees. Every year, countless hours and significant resources are lost because of poorly written code.",
                    coverGradientIndex = 1,
                    pdfFileName = "clean_code.pdf",
                    totalPages = 15,
                    rating = 4.7,
                    publishedYear = 2008,
                    isFeatured = true,
                    isPopular = true
                ),
                Book(
                    id = 3,
                    title = "Deep Work",
                    author = "Cal Newport",
                    category = "Self Help",
                    description = "Rules for Focused Success in a Distracted World. Deep work is the ability to focus without distraction on a cognitively demanding task. It's a skill that allows you to quickly master complicated information and produce better results in less time.",
                    coverGradientIndex = 2,
                    pdfFileName = "deep_work.pdf",
                    totalPages = 10,
                    rating = 4.6,
                    publishedYear = 2016,
                    isFeatured = true,
                    isPopular = true
                ),
                Book(
                    id = 4,
                    title = "The Psychology of Money",
                    author = "Morgan Housel",
                    category = "Finance",
                    description = "Timeless lessons on wealth, greed, and happiness. Doing well with money isn't necessarily about what you know. It's about how you behave. And behavior is hard to teach, even to really smart people.",
                    coverGradientIndex = 3,
                    pdfFileName = "psychology_of_money.pdf",
                    totalPages = 14,
                    rating = 4.7,
                    publishedYear = 2020,
                    isFeatured = false,
                    isPopular = true
                ),
                Book(
                    id = 5,
                    title = "Brief Answers to Big Questions",
                    author = "Stephen Hawking",
                    category = "Science",
                    description = "The world-famous cosmologist and best-selling author shares his final thoughts on the universe's biggest questions, exploring space, black holes, time travel, and the future of humanity.",
                    coverGradientIndex = 4,
                    pdfFileName = "brief_answers.pdf",
                    totalPages = 12,
                    rating = 4.9,
                    publishedYear = 2018,
                    isFeatured = true,
                    isPopular = false
                ),
                Book(
                    id = 6,
                    title = "Sapiens: A Brief History",
                    author = "Yuval Noah Harari",
                    category = "History",
                    description = "From a renowned historian comes a groundbreaking narrative of humanity's creation and evolution that explores how biology and history have defined us and enhanced our understanding of what it means to be human.",
                    coverGradientIndex = 5,
                    pdfFileName = "sapiens_history.pdf",
                    totalPages = 16,
                    rating = 4.8,
                    publishedYear = 2014,
                    isFeatured = false,
                    isPopular = true
                ),
                Book(
                    id = 7,
                    title = "To Kill a Mockingbird",
                    author = "Harper Lee",
                    category = "Fiction",
                    description = "Compassionate, dramatic, and deeply moving, To Kill A Mockingbird takes readers to the roots of human behavior - to innocence and experience, kindness and cruelty, love and hatred.",
                    coverGradientIndex = 0,
                    pdfFileName = "mockingbird.pdf",
                    totalPages = 11,
                    rating = 4.9,
                    publishedYear = 1960,
                    isFeatured = true,
                    isPopular = true
                ),
                Book(
                    id = 8,
                    title = "The Pragmatic Programmer",
                    author = "Andrew Hunt & David Thomas",
                    category = "Technology",
                    description = "The Pragmatic Programmer cuts through the increasing specialization and technicalities of modern software development to examine the core process—taking a requirement and producing working, maintainable code.",
                    coverGradientIndex = 1,
                    pdfFileName = "pragmatic_programmer.pdf",
                    totalPages = 14,
                    rating = 4.8,
                    publishedYear = 1999,
                    isFeatured = false,
                    isPopular = false
                )
            )

            bookDao.insertBooks(sampleBooks)
        }
    }
}
