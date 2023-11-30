package com.example.preteirb.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import com.example.compose.AppTheme
import com.example.preteirb.model.PreteirbAppViewModel
import com.example.preteirb.ui.navigation.appNavGraph
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

    var startDestination by remember { mutableStateOf(SearchDestination.route) }
    LaunchedEffect(viewModel.isLoggedIn) {
        startDestination =
            if (viewModel.isLoggedIn.first() && viewModel.profileId.first() != 0) {
                SearchDestination.route
            } else {
                ProfileSelectionDestination.route
            }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        appNavGraph(appState)
    }
}

@Preview
@Composable
fun PreteirbAppPreview() {
    AppTheme {
        PreteirbApp()
    }
}