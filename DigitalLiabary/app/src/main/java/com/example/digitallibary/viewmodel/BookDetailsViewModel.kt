package com.example.digitallibary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.models.ReadingHistory
import com.example.digitallibary.data.repository.BookRepository
import com.example.digitallibary.utils.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val bookRepository: BookRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book.asStateFlow()

    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()

    private val _readingHistory = MutableStateFlow<ReadingHistory?>(null)
    val readingHistory: StateFlow<ReadingHistory?> = _readingHistory.asStateFlow()

    fun loadBookDetails(bookId: Long) {
        val userId = preferenceManager.loggedInUserId
        viewModelScope.launch {
            val bookData = bookRepository.getBookByIdSingle(bookId)
            _book.value = bookData

            bookRepository.isBookmarked(userId, bookId).collect { bookmarked ->
                _isBookmarked.value = bookmarked
            }
        }
        viewModelScope.launch {
            bookRepository.getHistoryForBook(userId, bookId).collect { history ->
                _readingHistory.value = history
            }
        }
    }

    fun toggleBookmark() {
        val currentBook = _book.value ?: return
        val userId = preferenceManager.loggedInUserId
        viewModelScope.launch {
            val newState = bookRepository.toggleBookmark(userId, currentBook.id)
            _isBookmarked.value = newState
        }
    }
}
