package com.example.campuslostfound.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")

    // Bottom Nav Destinations
    object Home : Screen("home", "Home", Icons.Default.Home)
    object MyPosts : Screen("my_posts", "My Posts", Icons.Default.Assignment)
    object ReportChoice : Screen("report_choice", "Report", Icons.Default.AddCircle)
    object Messages : Screen("messages", "Messages", Icons.Default.Chat)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)

    // Additional Detail / Flow Destinations
    object ReportLost : Screen("report_lost")
    object ReportFound : Screen("report_found")
    object ItemDetails : Screen("item_details/{itemId}") {
        fun createRoute(itemId: Long) = "item_details/$itemId"
    }
    object ChatDetails : Screen("chat_details/{itemId}/{itemName}/{otherUserName}") {
        fun createRoute(itemId: Long, itemName: String, otherUserName: String) =
            "chat_details/$itemId/${itemName.ifBlank { "Item" }}/${otherUserName.ifBlank { "Student" }}"
    }
    object EditProfile : Screen("edit_profile")
    object AboutApp : Screen("about_app")
    object HelpSupport : Screen("help_support")
    object Notifications : Screen("notifications")
    object SearchResults : Screen("search_results/{query}/{filter}") {
        fun createRoute(query: String, filter: String) =
            "search_results/${query.ifBlank { "ALL" }}/${filter.ifBlank { "All" }}"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.MyPosts,
    Screen.ReportChoice,
    Screen.Messages,
    Screen.Profile
)
