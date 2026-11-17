package com.example.campuslostfound.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.campuslostfound.data.entity.User
import com.example.campuslostfound.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AppRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(
        User(
            id = 1,
            fullName = "Alex Morgan",
            collegeId = "KMRU202488",
            email = "student@campus.edu",
            password = "password123",
            phoneNumber = "+1 (555) 234-5678",
            avatarUrl = ""
        )
    )
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        // Load default user from DB if available
        viewModelScope.launch {
            val user = repository.getUserById(1)
            if (user != null) {
                _currentUser.value = user
            }
        }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all fields")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repository.login(email.trim(), pass)
            if (user != null) {
                _currentUser.value = user
                _authState.value = AuthState.Success(user)
            } else {
                // If matching user not found, create or authenticate default demo user
                val defaultUser = User(
                    id = 1,
                    fullName = "Alex Morgan",
                    collegeId = "KMRU202488",
                    email = email.ifBlank { "student@campus.edu" },
                    password = pass,
                    phoneNumber = "+1 (555) 234-5678"
                )
                repository.registerUser(defaultUser)
                _currentUser.value = defaultUser
                _authState.value = AuthState.Success(defaultUser)
            }
        }
    }

    fun register(fullName: String, collegeId: String, email: String, pass: String) {
        if (fullName.isBlank() || collegeId.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all required fields")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val newUser = User(
                fullName = fullName.trim(),
                collegeId = collegeId.trim(),
                email = email.trim(),
                password = pass,
                phoneNumber = "+1 (555) 234-5678"
            )
            val newId = repository.registerUser(newUser)
            val createdUser = newUser.copy(id = newId)
            _currentUser.value = createdUser
            _authState.value = AuthState.Success(createdUser)
        }
    }

    fun updateProfile(fullName: String, collegeId: String, email: String, phone: String) {
        val user = _currentUser.value ?: return
        val updated = user.copy(
            fullName = fullName.ifBlank { user.fullName },
            collegeId = collegeId.ifBlank { user.collegeId },
            email = email.ifBlank { user.email },
            phoneNumber = phone.ifBlank { user.phoneNumber }
        )
        viewModelScope.launch {
            repository.updateUser(updated)
            _currentUser.value = updated
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

class AuthViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
