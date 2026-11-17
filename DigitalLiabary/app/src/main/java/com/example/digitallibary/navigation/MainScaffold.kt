package com.example.digitallibary.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.digitallibary.ui.theme.NavyDark
import com.example.digitallibary.ui.theme.NavyPrimary
import com.example.digitallibary.ui.theme.PurpleAccent
import com.example.digitallibary.viewmodel.AuthViewModel
import com.example.digitallibary.viewmodel.BookDetailsViewModel
import com.example.digitallibary.viewmodel.ExploreViewModel
import com.example.digitallibary.viewmodel.HomeViewModel
import com.example.digitallibary.viewmodel.LibraryViewModel
import com.example.digitallibary.viewmodel.PdfReaderViewModel
import com.example.digitallibary.viewmodel.ProfileViewModel

@Composable
fun MainScaffold(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    exploreViewModel: ExploreViewModel,
    bookDetailsViewModel: BookDetailsViewModel,
    pdfReaderViewModel: PdfReaderViewModel,
    libraryViewModel: LibraryViewModel,
    profileViewModel: ProfileViewModel
) {
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Library,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show BottomBar only on main 4 tabs
    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = NavyDark,
                    contentColor = Color.White
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) PurpleAccent else Color.White.copy(alpha = 0.6f)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PurpleAccent else Color.White.copy(alpha = 0.6f)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = NavyPrimary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            authViewModel = authViewModel,
            homeViewModel = homeViewModel,
            exploreViewModel = exploreViewModel,
            bookDetailsViewModel = bookDetailsViewModel,
            pdfReaderViewModel = pdfReaderViewModel,
            libraryViewModel = libraryViewModel,
            profileViewModel = profileViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
