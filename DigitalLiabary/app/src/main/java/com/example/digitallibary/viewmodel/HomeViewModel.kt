package com.example.digitallibary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.models.Category
import com.example.digitallibary.data.models.User
import com.example.digitallibary.data.repository.BookRepository
import com.example.digitallibary.data.repository.UserRepository
import com.example.digitallibary.utils.PreferenceManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val currentUser: StateFlow<User?> =
        userRepository.getUser(preferenceManager.loggedInUserId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val featuredBooks: StateFlow<List<Book>> =
        bookRepository.getFeaturedBooks()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val popularBooks: StateFlow<List<Book>> =
        bookRepository.getPopularBooks()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentlyAddedBooks: StateFlow<List<Book>> =
        bookRepository.getAllBooks()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: List<Category> = listOf(
        Category("1", "All", "apps"),
        Category("2", "Fiction", "book"),
        Category("3", "Science", "science"),
        Category("4", "Technology", "laptop"),
        Category("5", "History", "history"),
        Category("6", "Self Help", "psychology")
    )
}
