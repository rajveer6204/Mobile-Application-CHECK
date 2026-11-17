package com.example.digitallibary.ui.screens.explore

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitallibary.ui.components.BookListItem
import com.example.digitallibary.ui.components.CategoryChip
import com.example.digitallibary.ui.components.EmptyStateComponent
import com.example.digitallibary.ui.components.SearchBarComponent
import com.example.digitallibary.ui.theme.NavyPrimary
import com.example.digitallibary.viewmodel.ExploreViewModel
import com.example.digitallibary.viewmodel.SortOption

@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    onBookClick: (Long) -> Unit
) {
    val searchQuery by exploreViewModel.searchQuery.collectAsState()
    val selectedCategory by exploreViewModel.selectedCategory.collectAsState()
    val sortOption by exploreViewModel.sortOption.collectAsState()
    val books by exploreViewModel.books.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    val categories = listOf("All", "Fiction", "Science", "Technology", "History", "Self Help", "Finance")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(NavyPrimary)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore Books",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            SearchBarComponent(
                query = searchQuery,
                onQueryChange = { exploreViewModel.updateSearchQuery(it) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { cat ->
                CategoryChip(
                    name = cat,
                    isSelected = selectedCategory == cat,
                    onClick = { exploreViewModel.selectCategory(cat) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sort Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${books.size} Books Found",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showSortMenu = true }
                ) {
                    Text(
                        text = "Sort by: ${sortOption.name.lowercase().capitalize()}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Sort options",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Popularity") },
                        onClick = {
                            exploreViewModel.setSortOption(SortOption.POPULAR)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Book Title") },
                        onClick = {
                            exploreViewModel.setSortOption(SortOption.TITLE)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Author Name") },
                        onClick = {
                            exploreViewModel.setSortOption(SortOption.AUTHOR)
                            showSortMenu = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Book Listing
        if (books.isEmpty()) {
            EmptyStateComponent(
                title = "No Books Found",
                subtitle = "Try searching for a different title, author, or category."
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books) { book ->
                    BookListItem(
                        book = book,
                        onClick = { onBookClick(book.id) }
                    )
                }
            }
        }
    }
}
