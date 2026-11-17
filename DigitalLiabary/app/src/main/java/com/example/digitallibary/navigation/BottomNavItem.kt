package com.example.digitallibary.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home)
    object Explore : BottomNavItem(Screen.Explore.route, "Explore", Icons.Default.Search)
    object Library : BottomNavItem(Screen.MyLibrary.route, "Library", Icons.Default.Bookmark)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}
