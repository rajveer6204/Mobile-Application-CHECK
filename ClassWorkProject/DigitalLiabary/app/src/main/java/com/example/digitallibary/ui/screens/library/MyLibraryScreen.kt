package com.example.digitallibary.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitallibary.data.models.BookWithProgress
import com.example.digitallibary.ui.components.BookCoverThumbnail
import com.example.digitallibary.ui.components.BookListItem
import com.example.digitallibary.ui.components.EmptyStateComponent
import com.example.digitallibary.ui.theme.NavyPrimary
import com.example.digitallibary.ui.theme.PurpleAccent
import com.example.digitallibary.viewmodel.LibraryTab
import com.example.digitallibary.viewmodel.LibraryViewModel

@Composable
fun MyLibraryScreen(
    libraryViewModel: LibraryViewModel,
    onBookClick: (Long) -> Unit,
    onReadNowClick: (Long) -> Unit
) {
    val selectedTab by libraryViewModel.selectedTab.collectAsState()
    val bookmarkedBooks by libraryViewModel.bookmarkedBooks.collectAsState()
    val readingHistory by libraryViewModel.readingHistory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(NavyPrimary)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "My Library",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = PurpleAccent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                    color = PurpleAccent,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == LibraryTab.SAVED,
                onClick = { libraryViewModel.selectTab(LibraryTab.SAVED) },
                text = {
                    Text(
                        text = "Saved Books (${bookmarkedBooks.size})",
                        fontWeight = if (selectedTab == LibraryTab.SAVED) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
            Tab(
                selected = selectedTab == LibraryTab.HISTORY,
                onClick = { libraryViewModel.selectTab(LibraryTab.HISTORY) },
                text = {
                    Text(
                        text = "Reading History (${readingHistory.size})",
                        fontWeight = if (selectedTab == LibraryTab.HISTORY) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            LibraryTab.SAVED -> {
                if (bookmarkedBooks.isEmpty()) {
                    EmptyStateComponent(
                        title = "No Bookmarks Saved",
                        subtitle = "Tap the bookmark icon on any book to save it to your library.",
                        icon = Icons.Default.Bookmark
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(bookmarkedBooks) { book ->
                            BookListItem(
                                book = book,
                                isBookmarked = true,
                                onBookmarkToggle = { libraryViewModel.removeBookmark(book.id) },
                                onClick = { onBookClick(book.id) }
                            )
                        }
                    }
                }
            }
            LibraryTab.HISTORY -> {
                if (readingHistory.isEmpty()) {
                    EmptyStateComponent(
                        title = "No Reading History",
                        subtitle = "Start reading any book to automatically track your progress here.",
                        icon = Icons.Default.History
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(readingHistory) { history ->
                            ReadingHistoryCard(
                                history = history,
                                onClick = { onBookClick(history.book.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingHistoryCard(
    history: BookWithProgress,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookCoverThumbnail(
                book = history.book,
                modifier = Modifier
                    .width(60.dp)
                    .height(76.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = history.book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = history.book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Reading Progress Bar
                LinearProgressIndicator(
                    progress = { (history.lastPageRead.toFloat() / history.totalPages.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PurpleAccent,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Page ${history.lastPageRead} of ${history.totalPages}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${history.progressPercentage}% Read",
                        style = MaterialTheme.typography.labelMedium,
                        color = PurpleAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
