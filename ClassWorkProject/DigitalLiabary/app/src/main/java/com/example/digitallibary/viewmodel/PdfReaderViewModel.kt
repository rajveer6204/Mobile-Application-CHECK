package com.example.digitallibary.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallibary.data.models.Book
import com.example.digitallibary.data.repository.BookRepository
import com.example.digitallibary.utils.PdfHelper
import com.example.digitallibary.utils.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File

class PdfReaderViewModel(
    private val bookRepository: BookRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    private val _pageBitmap = MutableStateFlow<Bitmap?>(null)
    val pageBitmap: StateFlow<Bitmap?> = _pageBitmap.asStateFlow()

    private val _zoomScale = MutableStateFlow(1.0f)
    val zoomScale: StateFlow<Float> = _zoomScale.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var pdfFile: File? = null

    fun loadPdf(context: Context, bookId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val bookData = bookRepository.getBookByIdSingle(bookId)
            if (bookData == null) {
                _isLoading.value = false
                return@launch
            }
            _book.value = bookData

            val file = PdfHelper.getOrGeneratePdfFile(context, bookData)
            pdfFile = file

            val count = PdfHelper.getPageCount(file)
            _totalPages.value = count

            // Restore last reading position
            val userId = preferenceManager.loggedInUserId
            val history = bookRepository.getHistoryForBook(userId, bookId).firstOrNull()
            val initialPage = history?.lastPageRead ?: 1
            _currentPage.value = initialPage.coerceIn(1, count)

            renderCurrentPage()
            _isLoading.value = false
        }
    }

    fun nextPage() {
        if (_currentPage.value < _totalPages.value) {
            _currentPage.value += 1
            renderCurrentPage()
            saveProgress()
        }
    }

    fun prevPage() {
        if (_currentPage.value > 1) {
            _currentPage.value -= 1
            renderCurrentPage()
            saveProgress()
        }
    }

    fun jumpToPage(page: Int) {
        val target = page.coerceIn(1, _totalPages.value)
        if (target != _currentPage.value) {
            _currentPage.value = target
            renderCurrentPage()
            saveProgress()
        }
    }

    fun zoomIn() {
        if (_zoomScale.value < 2.5f) {
            _zoomScale.value += 0.25f
        }
    }

    fun zoomOut() {
        if (_zoomScale.value > 0.75f) {
            _zoomScale.value -= 0.25f
        }
    }

    fun resetZoom() {
        _zoomScale.value = 1.0f
    }

    private fun renderCurrentPage() {
        val file = pdfFile ?: return
        viewModelScope.launch {
            // Render 0-indexed page for PdfRenderer
            val bitmap = PdfHelper.renderPdfPage(file, _currentPage.value - 1)
            _pageBitmap.value = bitmap
        }
    }

    private fun saveProgress() {
        val currentBook = _book.value ?: return
        val userId = preferenceManager.loggedInUserId
        viewModelScope.launch {
            bookRepository.updateReadingProgress(
                userId = userId,
                bookId = currentBook.id,
                pageRead = _currentPage.value,
                totalPages = _totalPages.value
            )
        }
    }
}
