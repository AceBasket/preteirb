package com.example.preteirb.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.PreteirbAppViewModel
import com.example.preteirb.ui.navigation.appNavGraph
import com.example.preteirb.ui.screens.auth.ChooseAuthenticationDestination
import com.example.preteirb.ui.screens.profile_selection.ProfileSelectionDestination
import com.example.preteirb.ui.screens.search.SearchDestination
import kotlinx.coroutines.launch

@Composable
fun PreteirbApp(
    modifier: Modifier = Modifier,
    viewModel: PreteirbAppViewModel = viewModel()
) {
    val appState = rememberAppState()

    //Create NavController
    val navController = appState.navController

    val startDestination = if (viewModel.isProfileSelected) {
        SearchDestination.route
    } else if (viewModel.isLoggedIn) {
        ProfileSelectionDestination.route
    } else {
        ChooseAuthenticationDestination.route
    }

    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                currentScreenTitle = appState.currentScreen.titleRes,
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
                switchProfile = {
                    coroutineScope.launch {
                        viewModel.switchProfile()
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
                isDisplayProfileIcon = appState.displayProfileIcon,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            BottomAppBar(
                destinations = appState.topLevelDestinations,
                currentDestination = appState.currentDestination,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                isDisplayBottomAppBar = appState.displayBottomAppBar,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                snackbar = { snackbarData ->
                    Snackbar(snackbarData)
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