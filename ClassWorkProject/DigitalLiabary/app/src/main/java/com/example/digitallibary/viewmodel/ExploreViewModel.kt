package com.example.digitallibary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.repository.BookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

enum class SortOption {
    POPULAR,
    TITLE,
    AUTHOR
}

class ExploreViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.POPULAR)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val books: StateFlow<List<Book>> = combine(
        _searchQuery,
        _selectedCategory,
        _sortOption
    ) { query, category, sort ->
        Triple(query, category, sort)
    }.flatMapLatest { (query, category, sort) ->
        val sourceFlow = if (query.isNotBlank()) {
            bookRepository.searchBooks(query.trim())
        } else {
            bookRepository.getBooksByCategory(category)
        }
        sourceFlow
    }.combine(_sortOption) { bookList, sort ->
        when (sort) {
            SortOption.POPULAR -> bookList.sortedByDescending { it.rating }
            SortOption.TITLE -> bookList.sortedBy { it.title }
            SortOption.AUTHOR -> bookList.sortedBy { it.author }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }
}
