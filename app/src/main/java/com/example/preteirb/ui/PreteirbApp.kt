package com.example.preteirb.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.PreteirbAppViewModel
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.navigation.appNavGraph
import com.example.preteirb.ui.screens.booking.BookItemDestination
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsDestination
import com.example.preteirb.ui.screens.items_owned.ListItemsDestination
import com.example.preteirb.ui.screens.profile_selection.ProfileSelectionDestination
import com.example.preteirb.ui.screens.search.SearchDestination
import kotlinx.coroutines.flow.first

@Composable
fun PreteirbApp(
    modifier: Modifier = Modifier,
    viewModel: PreteirbAppViewModel = viewModel()
) {
    val appState = rememberAppState()

    //Create NavController
    val navController = appState.navController

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get the current destination
    val currentScreen: NavigationDestination = backStackEntry?.destination?.route?.let { route ->
        Log.d("currentScreen", route)
        when (route) {
            SearchDestination.route -> SearchDestination
            ProfileSelectionDestination.route -> ProfileSelectionDestination
            BookItemDestination.routeWithArgs -> BookItemDestination
            ListItemsDestination.route -> ListItemsDestination
            ItemAndUsagesDetailsDestination.routeWithArgs -> ItemAndUsagesDetailsDestination
            else -> null
        }
    } ?: SearchDestination

    var startDestination by remember { mutableStateOf(currentScreen.route) }
    LaunchedEffect(viewModel.isLoggedIn) {
        startDestination =
            if (viewModel.isLoggedIn.first() && viewModel.profileId.first() != 0) {
                SearchDestination.route
            } else {
                ProfileSelectionDestination.route
            }
    }


    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                currentScreenTitle = currentScreen.titleRes,
                canNavigateBack = navController.previousBackStackEntry != null && !listOf(
                    SearchDestination.route,
                    ListItemsDestination.route,
                    ProfileSelectionDestination.route,
                ).contains(currentScreen.route),
                navigateUp = { navController.navigateUp() },
                navigateToProfileSelection = {
                    navController.navigate(ProfileSelectionDestination.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            BottomAppBar(
                destinations = appState.topLevelDestinations,
                currentDestination = appState.currentDestination,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier.padding(8.dp),
                snackbar = { snackbarData ->
                    Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.primary)
                }
            )
        },
    ) { innerPadding ->
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = screenModifier
        ) {
            appNavGraph(appState)
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