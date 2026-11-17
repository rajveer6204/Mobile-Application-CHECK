package com.example.digitallibary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.digitallibary.ui.screens.auth.LoginScreen
import com.example.digitallibary.ui.screens.auth.RegisterScreen
import com.example.digitallibary.ui.screens.details.BookDetailsScreen
import com.example.digitallibary.ui.screens.explore.ExploreScreen
import com.example.digitallibary.ui.screens.home.HomeScreen
import com.example.digitallibary.ui.screens.library.MyLibraryScreen
import com.example.digitallibary.ui.screens.onboarding.OnboardingScreen
import com.example.digitallibary.ui.screens.profile.ProfileScreen
import com.example.digitallibary.ui.screens.reader.PdfReaderScreen
import com.example.digitallibary.ui.screens.splash.SplashScreen
import com.example.digitallibary.viewmodel.AuthViewModel
import com.example.digitallibary.viewmodel.BookDetailsViewModel
import com.example.digitallibary.viewmodel.ExploreViewModel
import com.example.digitallibary.viewmodel.HomeViewModel
import com.example.digitallibary.viewmodel.LibraryViewModel
import com.example.digitallibary.viewmodel.PdfReaderViewModel
import com.example.digitallibary.viewmodel.ProfileViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    exploreViewModel: ExploreViewModel,
    bookDetailsViewModel: BookDetailsViewModel,
    pdfReaderViewModel: PdfReaderViewModel,
    libraryViewModel: LibraryViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                },
                onNavigateToExplore = { query ->
                    exploreViewModel.updateSearchQuery(query)
                    navController.navigate(Screen.Explore.route)
                }
            )
        }

        composable(Screen.Explore.route) {
            ExploreScreen(
                exploreViewModel = exploreViewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                }
            )
        }

        composable(Screen.MyLibrary.route) {
            MyLibraryScreen(
                libraryViewModel = libraryViewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                },
                onReadNowClick = { bookId ->
                    navController.navigate(Screen.PdfReader.createRoute(bookId))
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 1L
            BookDetailsScreen(
                bookId = bookId,
                detailsViewModel = bookDetailsViewModel,
                onBackClick = { navController.popBackStack() },
                onReadNowClick = { id ->
                    navController.navigate(Screen.PdfReader.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.PdfReader.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 1L
            PdfReaderScreen(
                bookId = bookId,
                readerViewModel = pdfReaderViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
