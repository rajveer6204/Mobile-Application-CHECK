package com.example.digitallibary

import android.app.Application
import com.example.digitallibary.data.database.AppDatabase

class DigitalLibraryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Pre-initialize database on application startup
        AppDatabase.getInstance(this)
    }
}
