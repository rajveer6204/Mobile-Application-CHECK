package com.example.digitallibary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.digitallibary.navigation.MainScaffold
import com.example.digitallibary.ui.theme.DigitalLibraryTheme
import com.example.digitallibary.viewmodel.AuthViewModel
import com.example.digitallibary.viewmodel.BookDetailsViewModel
import com.example.digitallibary.viewmodel.ExploreViewModel
import com.example.digitallibary.viewmodel.HomeViewModel
import com.example.digitallibary.viewmodel.LibraryViewModel
import com.example.digitallibary.viewmodel.PdfReaderViewModel
import com.example.digitallibary.viewmodel.ProfileViewModel
import com.example.digitallibary.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val factory = ViewModelFactory(applicationContext)

            val authViewModel: AuthViewModel = viewModel(factory = factory)
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            val exploreViewModel: ExploreViewModel = viewModel(factory = factory)
            val bookDetailsViewModel: BookDetailsViewModel = viewModel(factory = factory)
            val pdfReaderViewModel: PdfReaderViewModel = viewModel(factory = factory)
            val libraryViewModel: LibraryViewModel = viewModel(factory = factory)
            val profileViewModel: ProfileViewModel = viewModel(factory = factory)

            val isDarkTheme by profileViewModel.isDarkTheme.collectAsState()

            DigitalLibraryTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScaffold(
                        authViewModel = authViewModel,
                        homeViewModel = homeViewModel,
                        exploreViewModel = exploreViewModel,
                        bookDetailsViewModel = bookDetailsViewModel,
                        pdfReaderViewModel = pdfReaderViewModel,
                        libraryViewModel = libraryViewModel,
                        profileViewModel = profileViewModel
                    )
                }
            }
        }
    }
}
