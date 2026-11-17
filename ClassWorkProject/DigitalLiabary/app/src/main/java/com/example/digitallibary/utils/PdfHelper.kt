package com.example.digitallibary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.example.digitallibary.data.models.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object PdfHelper {

    suspend fun getOrGeneratePdfFile(context: Context, book: Book): File = withContext(Dispatchers.IO) {
        val booksDir = File(context.filesDir, "sample_pdfs").apply { if (!exists()) mkdirs() }
        val pdfFile = File(booksDir, book.pdfFileName)

        if (!pdfFile.exists() || pdfFile.length() == 0L) {
            createSamplePdf(pdfFile, book)
        }
        pdfFile
    }

    private fun createSamplePdf(file: File, book: Book) {
        val pdfDocument = PdfDocument()
        val pageWidth = 595 // Standard A4 width in points
        val pageHeight = 842 // Standard A4 height in points
        val totalPages = book.totalPages

        for (p in 1..totalPages) {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, p).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            // Background
            canvas.drawColor(Color.rgb(250, 250, 252))

            // Decorative top header bar
            val topBarPaint = Paint().apply {
                color = Color.rgb(23, 37, 84) // Navy Blue
                style = Paint.Style.FILL
            }
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 50f, topBarPaint)

            // Header text
            val headerTextPaint = Paint().apply {
                color = Color.WHITE
                textSize = 16f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }
            canvas.drawText("Digital Library - ${book.category}", 30f, 32f, headerTextPaint)

            // Page title / Chapter
            val titlePaint = Paint().apply {
                color = Color.rgb(23, 37, 84)
                textSize = 24f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }
            canvas.drawText(
                if (p == 1) book.title else "Chapter ${p - 1}: ${getChapterTitle(p - 1)}",
                40f,
                110f,
                titlePaint
            )

            // Subtitle / Author
            val subtitlePaint = Paint().apply {
                color = Color.rgb(124, 58, 237) // Purple Accent
                textSize = 16f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                isAntiAlias = true
            }
            canvas.drawText("By ${book.author}", 40f, 140f, subtitlePaint)

            // Horizontal divider line
            val dividerPaint = Paint().apply {
                color = Color.rgb(226, 232, 240)
                strokeWidth = 2f
            }
            canvas.drawLine(40f, 160f, (pageWidth - 40).toFloat(), 160f, dividerPaint)

            // Main Body Content Paragraphs
            val bodyPaint = Paint().apply {
                color = Color.rgb(30, 41, 59)
                textSize = 14f
                typeface = Typeface.DEFAULT
                isAntiAlias = true
            }

            val paragraphs = getSampleParagraphs(book, p)
            var currentY = 190f
            for (paragraph in paragraphs) {
                val lines = breakTextIntoLines(paragraph, bodyPaint, (pageWidth - 80).toFloat())
                for (line in lines) {
                    canvas.drawText(line, 40f, currentY, bodyPaint)
                    currentY += 22f
                }
                currentY += 14f
            }

            // Page Footer
            val footerPaint = Paint().apply {
                color = Color.rgb(148, 163, 184)
                textSize = 12f
                isAntiAlias = true
            }
            canvas.drawText("Page $p of $totalPages", (pageWidth / 2 - 30).toFloat(), (pageHeight - 30).toFloat(), footerPaint)
            canvas.drawText("Digital Library Reader", 40f, (pageHeight - 30).toFloat(), footerPaint)

            pdfDocument.finishPage(page)
        }

        try {
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
        } finally {
            pdfDocument.close()
        }
    }

    suspend fun renderPdfPage(pdfFile: File, pageIndex: Int): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)
            if (pageIndex < 0 || pageIndex >= pdfRenderer.pageCount) {
                pdfRenderer.close()
                fileDescriptor.close()
                return@withContext null
            }

            val page = pdfRenderer.openPage(pageIndex)
            val scale = 2 // Higher resolution for crisp text
            val bitmap = Bitmap.createBitmap(
                page.width * scale,
                page.height * scale,
                Bitmap.Config.ARGB_8888
            )

            // Render page onto canvas with white background
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            page.close()
            pdfRenderer.close()
            fileDescriptor.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getPageCount(pdfFile: File): Int = withContext(Dispatchers.IO) {
        try {
            val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)
            val count = pdfRenderer.pageCount
            pdfRenderer.close()
            fileDescriptor.close()
            count
        } catch (e: Exception) {
            1
        }
    }

    private fun getChapterTitle(chapterIndex: Int): String {
        val titles = listOf(
            "Small Changes, Big Results",
            "How Your Habits Shape Your Identity",
            "The 4 Simple Steps to Building Better Habits",
            "The Man Who Didn't Look Right",
            "The Best Way to Start a New Habit",
            "Motivation is Overrated; Environment Matters More",
            "The Secret to Self-Control",
            "How to Make a Habit Irresistible",
            "The Role of Family and Friends in Shaping Habits",
            "How to Find and Fix the Causes of Bad Habits"
        )
        return titles.getOrElse((chapterIndex - 1) % titles.size) { "Core Principles" }
    }

    private fun getSampleParagraphs(book: Book, pageNum: Int): List<String> {
        if (pageNum == 1) {
            return listOf(
                "Welcome to \"${book.title}\" by ${book.author}.",
                "Category: ${book.category} | Published: ${book.publishedYear}",
                "Synopsis:",
                book.description,
                "You are reading this book inside the Digital Library Android Application. Use the navigation buttons below or page controls to flip through pages, zoom in and out, and save your progress automatically."
            )
        }
        return listOf(
            "You do not rise to the level of your goals. You fall to the level of your systems. Your goal is your desired outcome, but your system is the collection of daily habits and processes that lead you there.",
            "Changes that seem small and unimportant at first will compound into remarkable results if you are willing to stick with them for years. We all deal with setbacks, but in the long run, the quality of our lives often depends on the quality of our habits.",
            "With the same habits, you'll end up with the same results. But with better habits, anything is possible. Focus not on what you want to achieve, but who you wish to become."
        )
    }

    private fun breakTextIntoLines(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "${currentLine} $word"
            val width = paint.measureText(testLine)
            if (width <= maxWidth) {
                currentLine.append(if (currentLine.isEmpty()) word else " $word")
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine.toString())
                }
                currentLine = StringBuilder(word)
            }
        }
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }
        return lines
    }
}
