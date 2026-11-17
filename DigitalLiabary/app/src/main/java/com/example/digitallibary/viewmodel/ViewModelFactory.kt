package com.example.digitallibary.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digitallibary.data.database.AppDatabase
import com.example.digitallibary.data.repository.BookRepository
import com.example.digitallibary.data.repository.UserRepository
import com.example.digitallibary.utils.PreferenceManager

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val database = AppDatabase.getInstance(context)
    private val userRepository = UserRepository(database.userDao())
    private val bookRepository = BookRepository(
        database.bookDao(),
        database.bookmarkDao(),
        database.readingHistoryDao()
    )
    private val preferenceManager = PreferenceManager(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository, preferenceManager) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(bookRepository, userRepository, preferenceManager) as T
            }
            modelClass.isAssignableFrom(ExploreViewModel::class.java) -> {
                ExploreViewModel(bookRepository) as T
            }
            modelClass.isAssignableFrom(BookDetailsViewModel::class.java) -> {
                BookDetailsViewModel(bookRepository, preferenceManager) as T
            }
            modelClass.isAssignableFrom(PdfReaderViewModel::class.java) -> {
                PdfReaderViewModel(bookRepository, preferenceManager) as T
            }
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> {
                LibraryViewModel(bookRepository, preferenceManager) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, preferenceManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
