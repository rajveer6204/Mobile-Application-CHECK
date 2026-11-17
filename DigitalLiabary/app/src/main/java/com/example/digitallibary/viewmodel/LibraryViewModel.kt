package com.example.digitallibary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.models.BookWithProgress
import com.example.digitallibary.data.repository.BookRepository
import com.example.digitallibary.utils.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class LibraryTab {
    SAVED,
    HISTORY
}

class LibraryViewModel(
    private val bookRepository: BookRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(LibraryTab.SAVED)
    val selectedTab: StateFlow<LibraryTab> = _selectedTab.asStateFlow()

    private val userId = preferenceManager.loggedInUserId

    val bookmarkedBooks: StateFlow<List<Book>> =
        bookRepository.getBookmarkedBooks(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingHistory: StateFlow<List<BookWithProgress>> =
        bookRepository.getUserReadingHistoryWithDetails(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: LibraryTab) {
        _selectedTab.value = tab
    }

    fun removeBookmark(bookId: Long) {
        viewModelScope.launch {
            bookRepository.toggleBookmark(userId, bookId)
        }
    }
}
