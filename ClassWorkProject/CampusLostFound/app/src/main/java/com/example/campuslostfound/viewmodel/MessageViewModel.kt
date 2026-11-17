package com.example.campuslostfound.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.campuslostfound.data.entity.Message
import com.example.campuslostfound.data.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageViewModel(private val repository: AppRepository) : ViewModel() {

    val conversations: StateFlow<List<Message>> = repository.getConversations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getMessagesForItem(itemId: Long): StateFlow<List<Message>> {
        return repository.getMessagesForItem(itemId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun sendMessage(
        itemId: Long,
        itemName: String,
        receiverName: String,
        text: String,
        senderId: Long = 1,
        receiverId: Long = 2
    ) {
        if (text.isBlank()) return
        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            val userMsg = Message(
                senderId = senderId,
                receiverId = receiverId,
                itemId = itemId,
                itemName = itemName,
                otherUserName = receiverName,
                message = text.trim(),
                timestamp = currentTime,
                isFromUser = true
            )
            repository.insertMessage(userMsg)

            // Simulate instant friendly campus response after 1.5 seconds
            delay(1200)
            val replyMsg = Message(
                senderId = receiverId,
                receiverId = senderId,
                itemId = itemId,
                itemName = itemName,
                otherUserName = receiverName,
                message = getAutoReply(text, itemName),
                timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
                isFromUser = false
            )
            repository.insertMessage(replyMsg)
        }
    }

    private fun getAutoReply(userText: String, item: String): String {
        val lower = userText.lowercase()
        return when {
            lower.contains("where") || lower.contains("meet") ->
                "We can meet at the Campus Library entrance or Student Center around 2:00 PM today!"
            lower.contains("hello") || lower.contains("hi") ->
                "Hello! Thanks for reaching out regarding '$item'. How can I help verify ownership?"
            lower.contains("proof") || lower.contains("color") ->
                "Yes, I can confirm those details match! I have the item safe with me."
            else ->
                "Got your message! I am currently near the administration building. Let me know when you are available to meet."
        }
    }
}

class MessageViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
