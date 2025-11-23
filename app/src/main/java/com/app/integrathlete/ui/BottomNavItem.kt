package com.app.integrathlete.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.integrathlete.loader.JsonHelper
import com.app.integrathlete.model.Supplement
import com.app.integrathlete.model.SupplementViewModel
import com.app.integrathlete.model.UserProfileViewModel
import com.app.integrathlete.repository.UserPreferencesRepository
import kotlinx.serialization.Serializable

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Supplements : BottomNavItem("supplements", "Integratori", Icons.AutoMirrored.Filled.ListAlt)
    object Favorites : BottomNavItem("favorites", "Preferiti", Icons.Default.Favorite)

    object Profile : BottomNavItem("profile", "Profilo", Icons.Default.AccountCircle)

    object Suggestions : BottomNavItem("suggestions", "Suggeriti", Icons.Default.Star)


}

@Composable
fun MainScreenWithBottomNav(
    allSupplements: List<Supplement>
) {
    val navController = rememberNavController()
    val viewModel: SupplementViewModel = viewModel()
    val context = LocalContext.current
    val userViewModel: UserProfileViewModel = hiltViewModel()

    val sportList=  remember { JsonHelper.loadSports(context)}

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryFlow.collectAsState(
                    initial = navController.currentBackStackEntry
                ).value?.destination?.route?.substringBefore("/")

                listOf(
                    BottomNavItem.Suggestions,
                    BottomNavItem.Supplements,
                    BottomNavItem.Favorites,
                    BottomNavItem.Profile
                ).forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Suggestions.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Suggestions.route) {
                SupplementSuggestionsScreen(
                    supplements = allSupplements,
                    onItemClick= {selected ->
                        navController.navigate("supplement_detail/${selected.id}")
                    }
                )
            }


            composable(BottomNavItem.Supplements.route) {
                SupplementList(
                    supplements = allSupplements,
                    onItemClick = { selected ->
                        navController.navigate("supplement_detail/${selected.id}")
                    }
                )
            }

            composable(BottomNavItem.Favorites.route) {
                val favorites = viewModel.favorites.collectAsState().value.toList()
                FavoritesScreen(
                    favoriteSupplements = favorites,
                    onSupplementClick = { selected ->
                        navController.navigate("supplement_detail/${selected.id}")
                    }
                )
            }

            composable(
                route = "supplement_detail/{supplementId}",
                arguments = listOf(navArgument("supplementId") { type = NavType.IntType })
            ) { backStackEntry ->
                val supplementId = backStackEntry.arguments?.getInt("supplementId")
                val supplement = allSupplements.find { it.id == supplementId }

                if (supplement != null) {
                    SupplementDetailScreenWithTopBar(
                        supplement = supplement,
                        onBackClick = { navController.popBackStack() },
                        viewModel = viewModel
                    )
                } else {
                    Text("Supplement not found")
                }
            }

            composable(
                route = "supplement_list/{sport}",
                arguments = listOf(navArgument("sport") { type = NavType.StringType })
            ) { backStackEntry ->
                val sport = backStackEntry.arguments?.getString("sport") ?: ""
                val filteredSupplements = allSupplements.filter { it.sports.contains(sport) }

                SupplementList(
                    supplements = filteredSupplements,
                    onItemClick = { selected ->
                        navController.navigate("supplement_detail/${selected.id}")
                    }
                )
            }
            composable(BottomNavItem.Profile.route) {
                val userViewModel: UserProfileViewModel = userViewModel
                val prefs = userViewModel.userPrefs.collectAsState().value

                ProfileScreen(
                    sport = prefs.sport,
                    frequency = prefs.frequency,
                    weightKg = prefs.weightKg,
                    heightCm = prefs.heightCm,
                    onEditClick = {
                        navController.navigate("edit_profile")
                    }
                )
            }
            composable("edit_profile") {
                ProfileSetupPage(
                    onComplete = { navController.popBackStack() }, // torna indietro al profilo
                    sportsList = sportList
                )
            }


        }
    }
}
