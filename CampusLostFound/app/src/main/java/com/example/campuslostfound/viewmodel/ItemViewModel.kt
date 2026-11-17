package com.example.campuslostfound.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.campuslostfound.data.entity.Item
import com.example.campuslostfound.data.entity.ItemStatus
import com.example.campuslostfound.data.entity.ItemType
import com.example.campuslostfound.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: AppRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedTypeFilter = MutableStateFlow("All") // "All", "LOST", "FOUND"
    val selectedTypeFilter: StateFlow<String> = _selectedTypeFilter.asStateFlow()

    val allItems: StateFlow<List<Item>> = repository.getAllItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lostItems: StateFlow<List<Item>> = repository.getItemsByType(ItemType.LOST)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val foundItems: StateFlow<List<Item>> = repository.getItemsByType(ItemType.FOUND)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredItems: StateFlow<List<Item>> = combine(
        allItems,
        _searchQuery,
        _selectedCategory,
        _selectedTypeFilter
    ) { items, query, category, filter ->
        items.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.itemName.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.location.contains(query, ignoreCase = true)

            val matchesCategory = category == "All" || item.category.equals(category, ignoreCase = true)

            val matchesFilter = when (filter.uppercase()) {
                "LOST" -> item.itemType == ItemType.LOST
                "FOUND" -> item.itemType == ItemType.FOUND
                else -> true
            }

            matchesQuery && matchesCategory && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSelectedTypeFilter(filter: String) {
        _selectedTypeFilter.value = filter
    }

    fun getItemsForUser(userId: Long, type: ItemType): StateFlow<List<Item>> {
        return repository.getItemsByUserAndType(userId, type)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getItemByIdFlow(itemId: Long): StateFlow<Item?> {
        val flow = MutableStateFlow<Item?>(null)
        viewModelScope.launch {
            repository.getItemByIdFlow(itemId).collect {
                flow.value = it
            }
        }
        return flow
    }

    fun reportItem(
        itemName: String,
        category: String,
        description: String,
        location: String,
        imageUri: String,
        itemType: ItemType,
        userId: Long,
        uploaderName: String,
        date: String,
        contactInfo: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val item = Item(
                itemName = itemName,
                category = category,
                description = description,
                location = location,
                imageUri = imageUri,
                itemType = itemType,
                userId = userId,
                uploaderName = uploaderName.ifBlank { "Campus Student" },
                date = date,
                status = ItemStatus.ACTIVE,
                contactInfo = contactInfo
            )
            repository.insertItem(item)
            onSuccess()
        }
    }

    fun updateStatus(itemId: Long, status: ItemStatus) {
        viewModelScope.launch {
            repository.updateItemStatus(itemId, status)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}

class ItemViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
