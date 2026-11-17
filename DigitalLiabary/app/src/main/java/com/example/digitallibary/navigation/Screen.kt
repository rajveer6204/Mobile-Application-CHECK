package com.example.digitallibary.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object MyLibrary : Screen("my_library")
    object Profile : Screen("profile")
    object BookDetails : Screen("book_details/{bookId}") {
        fun createRoute(bookId: Long) = "book_details/$bookId"
    }
    object PdfReader : Screen("pdf_reader/{bookId}") {
        fun createRoute(bookId: Long) = "pdf_reader/$bookId"
    }
}
