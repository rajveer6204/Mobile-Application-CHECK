package com.example.campuslostfound

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuslostfound.ui.navigation.MainAppNavigation
import com.example.campuslostfound.ui.theme.CampusLostFoundTheme
import com.example.campuslostfound.viewmodel.AuthViewModel
import com.example.campuslostfound.viewmodel.AuthViewModelFactory
import com.example.campuslostfound.viewmodel.ItemViewModel
import com.example.campuslostfound.viewmodel.ItemViewModelFactory
import com.example.campuslostfound.viewmodel.MessageViewModel
import com.example.campuslostfound.viewmodel.MessageViewModelFactory
import com.example.campuslostfound.viewmodel.NotificationViewModel
import com.example.campuslostfound.viewmodel.NotificationViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as CampusApp
        val repository = app.repository

        setContent {
            CampusLostFoundTheme {
                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))
                val itemViewModel: ItemViewModel = viewModel(factory = ItemViewModelFactory(repository))
                val messageViewModel: MessageViewModel = viewModel(factory = MessageViewModelFactory(repository))
                val notificationViewModel: NotificationViewModel = viewModel(factory = NotificationViewModelFactory(repository))

                MainAppNavigation(
                    authViewModel = authViewModel,
                    itemViewModel = itemViewModel,
                    messageViewModel = messageViewModel,
                    notificationViewModel = notificationViewModel
                )
            }
        }
    }
}
