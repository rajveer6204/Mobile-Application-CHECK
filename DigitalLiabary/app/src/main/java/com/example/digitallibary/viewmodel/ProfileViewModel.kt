package com.example.digitallibary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallibary.data.models.User
import com.example.digitallibary.data.repository.UserRepository
import com.example.digitallibary.utils.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val userId = preferenceManager.loggedInUserId

    val user: StateFlow<User?> = userRepository.getUser(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isDarkTheme = MutableStateFlow(preferenceManager.isDarkTheme)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _updateState = MutableStateFlow<String?>(null)
    val updateState: StateFlow<String?> = _updateState.asStateFlow()

    fun toggleTheme(enabled: Boolean) {
        preferenceManager.isDarkTheme = enabled
        _isDarkTheme.value = enabled
    }

    fun updateProfile(fullName: String, bio: String) {
        if (fullName.isBlank()) {
            _updateState.value = "Full Name cannot be empty."
            return
        }
        viewModelScope.launch {
            val result = userRepository.updateUserProfile(userId, fullName.trim(), bio.trim())
            result.onSuccess {
                _updateState.value = "Profile updated successfully!"
            }.onFailure { ex ->
                _updateState.value = ex.message ?: "Failed to update profile."
            }
        }
    }

    fun clearUpdateMessage() {
        _updateState.value = null
    }

    fun logout() {
        preferenceManager.clearSession()
    }
}
