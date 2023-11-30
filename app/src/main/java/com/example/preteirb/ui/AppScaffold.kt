package com.example.preteirb.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import com.example.preteirb.ui.navigation.NavigationDestination

@Composable
fun AppScaffold(
    appState: AppState,
    currentDestination: NavDestination,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToProfileSelection: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                currentScreenTitle = currentDestination.titleRes,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                navigateToProfileSelection = navigateToProfileSelection,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            BottomAppBar(
                destinations = listOf(
                    NavigationDestination.Search,
                    NavigationDestination.ListItems,
                ),
                onNavigateToDestination = appState.navigateToTopLevelDestination,
                currentDestination = currentDestination,
            )
        },
        content = content,
//        snackbarHost = {
//            SnackbarHost(
//                hostState = appState.snackbarHostState,
//                modifier = Modifier.padding(8.dp),
//                snackbar = { snackbarData ->
//                    Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.primary)
//                }
//            )
//        },
    )
}