package com.example.preteirb.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.preteirb.ui.AppState
import com.example.preteirb.ui.screens.ProfileDestination
import com.example.preteirb.ui.screens.ProfileScreen
import com.example.preteirb.ui.screens.ProfileSelectionDestination
import com.example.preteirb.ui.screens.ProfileSelectionScreen
import com.example.preteirb.ui.screens.booking.BookItemDestination
import com.example.preteirb.ui.screens.booking.BookItemScreen
import com.example.preteirb.ui.screens.list.ItemAndUsagesDetailsDestination
import com.example.preteirb.ui.screens.list.ItemAndUsagesDetailsScreen
import com.example.preteirb.ui.screens.list.ListItemsDestination
import com.example.preteirb.ui.screens.list.ListItemsScreen
import com.example.preteirb.ui.screens.newitemusage.ItemOwnedUsageEntryDestination
import com.example.preteirb.ui.screens.newitemusage.NewUsageScreen
import com.example.preteirb.ui.screens.search.SearchDestination
import com.example.preteirb.ui.screens.search.SearchScreen

fun NavGraphBuilder.appNavGraph(appState: AppState) {
    composable(route = SearchDestination.route) {
        SearchScreen(
            navigateToBookItem = { appState.navigate("${BookItemDestination.route}/${it}") },
        )
    }
    
    composable(route = ProfileDestination.route) {
        ProfileScreen(
            navigateToSelectProfile = { appState.navigate(ProfileSelectionDestination.route) },
        )
    }
    
    composable(route = ItemOwnedUsageEntryDestination.route) {
        NewUsageScreen(
            navigateToHomeScreen = {
                appState.clearAndNavigate(SearchDestination.route)
            },
        )
    }
    
    composable(route = ProfileSelectionDestination.route) {
        ProfileSelectionScreen(
            navigateToSearch = {
                appState.clearAndNavigate(SearchDestination.route)
            }, //TODO: footnote saying account was created
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
                appState.clearAndNavigate(SearchDestination.route)
            },
        )
    }
    
    composable(route = ListItemsDestination.route) {
        ListItemsScreen(
            navigateToItemDetails = { appState.navigate("${ItemAndUsagesDetailsDestination.route}/${it}") },
        )
    }
    
    composable(
        route = ItemAndUsagesDetailsDestination.routeWithArgs,
        arguments = listOf(navArgument(ItemAndUsagesDetailsDestination.itemIdArg) {
            type = NavType.IntType
        })
    ) {
        ItemAndUsagesDetailsScreen(
            navigateToBookItem = { appState.navigate("${BookItemDestination.route}/${it}") },
        )
    }
}