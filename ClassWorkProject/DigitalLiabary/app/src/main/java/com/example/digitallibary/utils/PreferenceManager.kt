package com.example.digitallibary.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("digital_library_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_ONBOARDING_COMPLETED = "is_onboarding_completed"
        private const val KEY_LOGGED_IN_USER_ID = "logged_in_user_id"
        private const val KEY_IS_DARK_THEME = "is_dark_theme"
    }

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_IS_ONBOARDING_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_ONBOARDING_COMPLETED, value).apply()

    var loggedInUserId: Long
        get() = prefs.getLong(KEY_LOGGED_IN_USER_ID, 1L) // default to seeded user 1
        set(value) = prefs.edit().putLong(KEY_LOGGED_IN_USER_ID, value).apply()

    var isDarkTheme: Boolean
        get() = prefs.getBoolean(KEY_IS_DARK_THEME, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_DARK_THEME, value).apply()

    fun clearSession() {
        prefs.edit().remove(KEY_LOGGED_IN_USER_ID).apply()
    }
}
