package com.example.campuslostfound

import android.app.Application
import com.example.campuslostfound.data.database.AppDatabase
import com.example.campuslostfound.data.repository.AppRepository

class CampusApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { AppRepository(database) }
}
