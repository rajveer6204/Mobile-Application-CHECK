package com.example.campuslostfound.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuslostfound.ui.components.CategoryChip
import com.example.campuslostfound.ui.components.CustomSearchBar
import com.example.campuslostfound.ui.components.ItemCard
import com.example.campuslostfound.ui.theme.BackgroundLight
import com.example.campuslostfound.ui.theme.FoundGreen
import com.example.campuslostfound.ui.theme.FoundGreenContainer
import com.example.campuslostfound.ui.theme.LostRed
import com.example.campuslostfound.ui.theme.LostRedContainer
import com.example.campuslostfound.ui.theme.PrimaryPurple
import com.example.campuslostfound.ui.theme.SurfaceWhite
import com.example.campuslostfound.ui.theme.TextMuted
import com.example.campuslostfound.ui.theme.TextPrimary
import com.example.campuslostfound.ui.theme.TextSecondary
import com.example.campuslostfound.viewmodel.AuthViewModel
import com.example.campuslostfound.viewmodel.ItemViewModel
import com.example.campuslostfound.viewmodel.NotificationViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    itemViewModel: ItemViewModel,
    notificationViewModel: NotificationViewModel,
    onNavigateToReportLost: () -> Unit,
    onNavigateToReportFound: () -> Unit,
    onNavigateToItemDetails: (Long) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSearch: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val items by itemViewModel.filteredItems.collectAsState()
    val searchQuery by itemViewModel.searchQuery.collectAsState()
    val selectedCategory by itemViewModel.selectedCategory.collectAsState()
    val selectedFilter by itemViewModel.selectedTypeFilter.collectAsState()
    val unreadNotifications by notificationViewModel.unreadCount.collectAsState()

    val categories = listOf("All", "Electronics", "Documents", "Keys", "Bags", "Books", "Clothing", "Others")
    val typeFilters = listOf("All", "LOST", "FOUND")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Top Header & Welcome Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, ${currentUser?.fullName ?: "Student"} 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Campus Lost & Found Hub",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                // Notification Bell Icon with Badge
                IconButton(onClick = onNavigateToNotifications) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifications > 0) {
                                Badge(containerColor = LostRed) {
                                    Text("$unreadNotifications", color = SurfaceWhite)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            CustomSearchBar(
                query = searchQuery,
                onQueryChange = { itemViewModel.setSearchQuery(it) },
                onSearch = { onNavigateToSearch(searchQuery, selectedFilter) }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Quick Action Banner Cards: Report Lost & Report Found
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Report Lost Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(95.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigateToReportLost() },
                        colors = CardDefaults.cardColors(containerColor = LostRedContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(LostRed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = SurfaceWhite,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Lost Something?",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LostRed
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Report lost item",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Report Found Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(95.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigateToReportFound() },
                        colors = CardDefaults.cardColors(containerColor = FoundGreenContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(FoundGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = SurfaceWhite,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Found Something?",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FoundGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Report found item",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Categories Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Categories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                category = category,
                                isSelected = category.equals(selectedCategory, ignoreCase = true),
                                onClick = { itemViewModel.setSelectedCategory(category) }
                            )
                        }
                    }
                }
            }

            // Type Filter Tabs (All, Lost, Found)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Campus Posts",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        typeFilters.forEach { filter ->
                            val isSelected = filter.equals(selectedFilter, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) PrimaryPurple else SurfaceWhite)
                                    .clickable { itemViewModel.setSelectedTypeFilter(filter) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = filter,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SurfaceWhite else TextSecondary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Posts List or Empty State
            if (items.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "No items",
                            tint = TextMuted,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No posts match your filters",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Try clearing search or switching categories",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                items(items) { item ->
                    ItemCard(
                        item = item,
                        onClick = { onNavigateToItemDetails(item.id) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
