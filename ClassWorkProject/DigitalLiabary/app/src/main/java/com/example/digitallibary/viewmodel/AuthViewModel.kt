package com.example.digitallibary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallibary.data.models.User
import com.example.digitallibary.data.repository.UserRepository
import com.example.digitallibary.utils.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Success(val user: User) : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all fields.")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.loginUser(email.trim(), pass)
            result.onSuccess { user ->
                preferenceManager.loggedInUserId = user.id
                _authState.value = AuthState.Success(user)
            }.onFailure { ex ->
                _authState.value = AuthState.Error(ex.message ?: "Login failed.")
            }
        }
    }

    fun register(name: String, email: String, pass: String, confirmPass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all required fields.")
            return
        }
        if (pass != confirmPass) {
            _authState.value = AuthState.Error("Passwords do not match.")
            return
        }
        if (pass.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters.")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.registerUser(name.trim(), email.trim(), pass)
            result.onSuccess { user ->
                preferenceManager.loggedInUserId = user.id
                _authState.value = AuthState.Success(user)
            }.onFailure { ex ->
                _authState.value = AuthState.Error(ex.message ?: "Registration failed.")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
