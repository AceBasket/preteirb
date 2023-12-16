package com.example.preteirb.ui

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.PreteirbAppViewModel
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.navigation.appNavGraph
import com.example.preteirb.ui.screens.booking.BookItemDestination
import com.example.preteirb.ui.screens.items_booked.ItemsBookedDestination
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsDestination
import com.example.preteirb.ui.screens.items_owned.ListItemsDestination
import com.example.preteirb.ui.screens.profile_selection.ProfileSelectionDestination
import com.example.preteirb.ui.screens.search.SearchDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun PreteirbApp(
    modifier: Modifier = Modifier,
    viewModel: PreteirbAppViewModel = viewModel()
) {
    val appState = rememberAppState()

    //Create NavController
    val navController = appState.navController

    // Get the current destination
    val currentScreen: NavigationDestination = appState.currentDestination?.route.let { route ->
        when (route) {
            SearchDestination.route -> SearchDestination
            ProfileSelectionDestination.route -> ProfileSelectionDestination
            BookItemDestination.routeWithArgs -> BookItemDestination
            ListItemsDestination.route -> ListItemsDestination
            ItemAndUsagesDetailsDestination.routeWithArgs -> ItemAndUsagesDetailsDestination
            ItemsBookedDestination.route -> ItemsBookedDestination
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

    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                currentScreenTitle = currentScreen.titleRes,
                canNavigateBack = navController.previousBackStackEntry != null
                        && !appState.topLevelDestinations.map { it.route }
                    .contains(appState.currentDestination?.route),
                navigateUp = { navController.navigateUp() },
                logOut = {
                    coroutineScope.launch {
                        viewModel.logOut()
                    }
                    appState.clearAndNavigate(ProfileSelectionDestination.route)
                },
                profileUiState = viewModel.profileUiState,
                onSaveChangesToProfile = {
                    coroutineScope.launch {
                        viewModel.saveProfileModifications()
                    }
                },
                updateProfile = viewModel::updateUiState,
                profile = viewModel.currentProfile,
                isSelectProfile = appState.currentDestination?.route == ProfileSelectionDestination.route,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            BottomAppBar(
                destinations = appState.topLevelDestinations,
                currentDestination = appState.currentDestination,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                isDisplayBottomAppBar = appState.currentDestination?.route != ProfileSelectionDestination.route,
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
            .padding(dimensionResource(id = R.dimen.padding_medium))
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