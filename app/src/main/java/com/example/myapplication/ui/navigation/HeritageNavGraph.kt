package com.example.myapplication.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.dialogs.WriteReviewDialog
import com.example.myapplication.ui.screens.ExploreScreen
import com.example.myapplication.ui.screens.FavoritesScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.SiteDetailScreen
import com.example.myapplication.ui.viewmodel.HeritageViewModel

@Composable
fun MainAppScreen(
    viewModel: HeritageViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val allSites by viewModel.allSites.collectAsState()
    val favoriteSites by viewModel.favoriteSites.collectAsState()
    val selectedSite by viewModel.selectedSite.collectAsState()
    val selectedSiteReviews by viewModel.selectedSiteReviews.collectAsState()
    val showWriteReviewDialog by viewModel.showWriteReviewDialog.collectAsState()

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.Favorites.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        if (currentRoute != screen.route) {
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
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    categories = viewModel.categories,
                    sites = allSites,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onCategorySelect = viewModel::onCategorySelect,
                    onSiteClick = { siteId ->
                        navController.navigate("detail/$siteId")
                    },
                    onFavoriteToggle = viewModel::toggleFavorite
                )
            }

            composable(Screen.Explore.route) {
                ExploreScreen(
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    categories = viewModel.categories,
                    sites = allSites,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onCategorySelect = viewModel::onCategorySelect,
                    onSiteClick = { siteId ->
                        navController.navigate("detail/$siteId")
                    },
                    onFavoriteToggle = viewModel::toggleFavorite
                )
            }

            composable(Screen.Favorites.route) {
                // Ensure favorites list is fresh
                LaunchedEffect(Unit) {
                    viewModel.loadFavorites()
                }
                FavoritesScreen(
                    favoriteSites = favoriteSites,
                    onSiteClick = { siteId ->
                        navController.navigate("detail/$siteId")
                    },
                    onFavoriteToggle = viewModel::toggleFavorite
                )
            }

            composable(
                route = "detail/{siteId}",
                arguments = listOf(navArgument("siteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                LaunchedEffect(siteId) {
                    viewModel.selectSite(siteId)
                }

                SiteDetailScreen(
                    site = selectedSite,
                    reviews = selectedSiteReviews,
                    onBackClick = { navController.popBackStack() },
                    onFavoriteToggle = viewModel::toggleFavorite,
                    onWriteReviewClick = {
                        viewModel.setShowWriteReviewDialog(true)
                    }
                )
            }
        }

        // Write Review Dialog
        if (showWriteReviewDialog && selectedSite != null) {
            val site = selectedSite!!
            WriteReviewDialog(
                siteName = site.name,
                onDismiss = { viewModel.setShowWriteReviewDialog(false) },
                onSubmit = { userName, rating, reviewText ->
                    viewModel.submitReview(
                        siteId = site.id,
                        userName = userName,
                        rating = rating,
                        reviewText = reviewText
                    )
                    Toast.makeText(context, "Review saved successfully!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
