package com.example.preteirb.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.AppViewModelProvider
import com.example.preteirb.model.PreteirbAppViewModel
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.screens.ProfileDestination
import com.example.preteirb.ui.screens.ProfileScreen
import com.example.preteirb.ui.screens.ProfileSelectionDestination
import com.example.preteirb.ui.screens.ProfileSelectionScreen
import com.example.preteirb.ui.screens.booking.BookItemDestination
import com.example.preteirb.ui.screens.booking.BookItemScreen
import com.example.preteirb.ui.screens.newitemusage.ItemOwnedUsageEntryDestination
import com.example.preteirb.ui.screens.newitemusage.NewUsageScreen
import com.example.preteirb.ui.screens.search.SearchDestination
import com.example.preteirb.ui.screens.search.SearchScreen
import kotlinx.coroutines.flow.first

@Composable
fun PreteirbApp(
    modifier: Modifier = Modifier,
    viewModel: PreteirbAppViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    //Create NavController
    val navController = rememberNavController()
    
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    
    // Get the current destination
    val currentScreen: NavigationDestination = backStackEntry?.destination?.route?.let { route ->
        when (route) {
            SearchDestination.route -> SearchDestination
            ProfileDestination.route -> ProfileDestination
            ProfileSelectionDestination.route -> ProfileSelectionDestination
            ItemOwnedUsageEntryDestination.route -> ItemOwnedUsageEntryDestination
            else -> null
        }
    } ?: SearchDestination
    
    var startDestination by remember { mutableStateOf(currentScreen.route) }
    
    LaunchedEffect(viewModel.isLoggedIn) {
        startDestination = if (viewModel.isLoggedIn.first()) {
            SearchDestination.route
        } else {
            ProfileSelectionDestination.route
        }
    }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            LoanAppTopBar(
                currentScreenTitle = currentScreen.titleRes,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            BottomLoanAppBar(
                navController = navController,
                backStackEntry = null,
                isDisplayBottomAppBar = navController.currentDestination?.route != ProfileSelectionDestination.route,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(route = SearchDestination.route) {
                SearchScreen(
                    navigateToBookItem = { navController.navigate("${BookItemDestination.route}/${it}") },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            
            composable(route = ProfileDestination.route) {
                ProfileScreen(
                    navigateToSelectProfile = { navController.navigate(ProfileSelectionDestination.route) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            composable(route = ItemOwnedUsageEntryDestination.route) {
                NewUsageScreen(
                    navigateToHomeScreen = {
                        // pop back stack till and including the given route
                        navController.popBackStack(ItemOwnedUsageEntryDestination.route, true)
                        navController.navigate(SearchDestination.route)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            composable(route = ProfileSelectionDestination.route) {
                ProfileSelectionScreen(
                    navigateToSearch = {
                        // pop back stack till and including the given route
                        navController.popBackStack(ProfileSelectionDestination.route, true)
                        navController.navigate(SearchDestination.route)
                    }, //TODO: footnote saying account was created
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            composable(
                route = BookItemDestination.routeWithArgs,
                arguments = listOf(navArgument(BookItemDestination.itemIdArg) {
                    type = NavType.IntType
                })
            ) {
                BookItemScreen(
                    navigateToHomeScreen = {
                        // pop back stack till and including the given route
                        navController.popBackStack(BookItemDestination.route, true)
                        navController.navigate(SearchDestination.route)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }
        
    }
}

@Preview
@Composable
fun PreteirbAppPreview() {
    AppTheme {
        PreteirbApp()
    }
}