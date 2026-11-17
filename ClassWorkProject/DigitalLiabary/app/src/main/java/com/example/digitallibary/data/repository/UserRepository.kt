package com.example.digitallibary.data.repository

import com.example.digitallibary.data.database.UserDao
import com.example.digitallibary.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(name: String, email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return@withContext Result.failure(Exception("An account with this email already exists."))
        }
        val newUser = User(
            fullName = name,
            email = email,
            passwordHash = password,
            bio = "Digital Library Member"
        )
        val id = userDao.insertUser(newUser)
        Result.success(newUser.copy(id = id))
    }

    suspend fun loginUser(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByEmail(email)
            ?: return@withContext Result.failure(Exception("No account found with this email."))

        if (user.passwordHash != password) {
            return@withContext Result.failure(Exception("Incorrect password. Please try again."))
        }
        Result.success(user)
    }

    fun getUser(userId: Long): Flow<User?> = userDao.getUserById(userId)

    suspend fun updateUserProfile(userId: Long, name: String, bio: String): Result<Unit> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByIdSingle(userId)
            ?: return@withContext Result.failure(Exception("User not found."))

        val updated = user.copy(fullName = name, bio = bio)
        userDao.updateUser(updated)
        Result.success(Unit)
    }
}
