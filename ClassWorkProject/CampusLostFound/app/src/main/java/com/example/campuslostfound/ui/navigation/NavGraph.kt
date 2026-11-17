package com.example.campuslostfound.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.campuslostfound.ui.components.BottomNavigationBar
import com.example.campuslostfound.ui.screens.AboutAppScreen
import com.example.campuslostfound.ui.screens.ChatDetailScreen
import com.example.campuslostfound.ui.screens.EditProfileScreen
import com.example.campuslostfound.ui.screens.HelpSupportScreen
import com.example.campuslostfound.ui.screens.HomeScreen
import com.example.campuslostfound.ui.screens.ItemDetailsScreen
import com.example.campuslostfound.ui.screens.LoginScreen
import com.example.campuslostfound.ui.screens.MessagesScreen
import com.example.campuslostfound.ui.screens.MyPostsScreen
import com.example.campuslostfound.ui.screens.NotificationsScreen
import com.example.campuslostfound.ui.screens.OnboardingScreen
import com.example.campuslostfound.ui.screens.ProfileScreen
import com.example.campuslostfound.ui.screens.ReportFoundScreen
import com.example.campuslostfound.ui.screens.ReportLostScreen
import com.example.campuslostfound.ui.screens.SearchResultsScreen
import com.example.campuslostfound.ui.screens.SignUpScreen
import com.example.campuslostfound.ui.screens.SplashScreen
import com.example.campuslostfound.viewmodel.AuthViewModel
import com.example.campuslostfound.viewmodel.ItemViewModel
import com.example.campuslostfound.viewmodel.MessageViewModel
import com.example.campuslostfound.viewmodel.NotificationViewModel

@Composable
fun MainAppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    itemViewModel: ItemViewModel,
    messageViewModel: MessageViewModel,
    notificationViewModel: NotificationViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.MyPosts.route,
        Screen.Messages.route,
        Screen.Profile.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        if (screen == Screen.ReportChoice) {
                            navController.navigate(Screen.ReportLost.route)
                        } else {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Splash Screen
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Onboarding Screen
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinishOnboarding = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // 3. Login Screen
            composable(Screen.Login.route) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate(Screen.SignUp.route)
                    }
                )
            }

            // 4. Sign Up Screen
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    authViewModel = authViewModel,
                    onSignUpSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // 5. Home Screen
            composable(Screen.Home.route) {
                HomeScreen(
                    authViewModel = authViewModel,
                    itemViewModel = itemViewModel,
                    notificationViewModel = notificationViewModel,
                    onNavigateToReportLost = { navController.navigate(Screen.ReportLost.route) },
                    onNavigateToReportFound = { navController.navigate(Screen.ReportFound.route) },
                    onNavigateToItemDetails = { id -> navController.navigate(Screen.ItemDetails.createRoute(id)) },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    onNavigateToSearch = { query, filter ->
                        navController.navigate(Screen.SearchResults.createRoute(query, filter))
                    }
                )
            }

            // 6. Report Lost Item Screen
            composable(Screen.ReportLost.route) {
                ReportLostScreen(
                    authViewModel = authViewModel,
                    itemViewModel = itemViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSubmitSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // 7. Report Found Item Screen
            composable(Screen.ReportFound.route) {
                ReportFoundScreen(
                    authViewModel = authViewModel,
                    itemViewModel = itemViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSubmitSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // 8. Item Details Screen
            composable(
                route = Screen.ItemDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.LongType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
                ItemDetailsScreen(
                    itemId = itemId,
                    itemViewModel = itemViewModel,
                    onBackClick = { navController.popBackStack() },
                    onContactOwner = { id, name, user ->
                        navController.navigate(Screen.ChatDetails.createRoute(id, name, user))
                    }
                )
            }

            // 9. My Posts Screen
            composable(Screen.MyPosts.route) {
                MyPostsScreen(
                    authViewModel = authViewModel,
                    itemViewModel = itemViewModel,
                    onNavigateToItemDetails = { id -> navController.navigate(Screen.ItemDetails.createRoute(id)) }
                )
            }

            // 10. Messages Screen
            composable(Screen.Messages.route) {
                MessagesScreen(
                    messageViewModel = messageViewModel,
                    onOpenChat = { id, item, user ->
                        navController.navigate(Screen.ChatDetails.createRoute(id, item, user))
                    }
                )
            }

            // 11. Chat Details Screen
            composable(
                route = Screen.ChatDetails.route,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.LongType },
                    navArgument("itemName") { type = NavType.StringType },
                    navArgument("otherUserName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
                val itemName = backStackEntry.arguments?.getString("itemName") ?: "Item"
                val otherUserName = backStackEntry.arguments?.getString("otherUserName") ?: "Student"

                ChatDetailScreen(
                    itemId = itemId,
                    itemName = itemName,
                    otherUserName = otherUserName,
                    messageViewModel = messageViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 12. Profile Screen
            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onNavigateToMyPosts = { navController.navigate(Screen.MyPosts.route) },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    onNavigateToHelpSupport = { navController.navigate(Screen.HelpSupport.route) },
                    onNavigateToAboutApp = { navController.navigate(Screen.AboutApp.route) },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 13. Edit Profile Screen
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    authViewModel = authViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }

            // 14. Notifications Screen
            composable(Screen.Notifications.route) {
                NotificationsScreen(
                    notificationViewModel = notificationViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 15. Help & Support Screen
            composable(Screen.HelpSupport.route) {
                HelpSupportScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 16. About App Screen
            composable(Screen.AboutApp.route) {
                AboutAppScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 17. Search Results Screen
            composable(
                route = Screen.SearchResults.route,
                arguments = listOf(
                    navArgument("query") { type = NavType.StringType },
                    navArgument("filter") { type = NavType.StringType }
                )
            ) {
                SearchResultsScreen(
                    itemViewModel = itemViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToItemDetails = { id -> navController.navigate(Screen.ItemDetails.createRoute(id)) }
                )
            }
        }
    }
}
