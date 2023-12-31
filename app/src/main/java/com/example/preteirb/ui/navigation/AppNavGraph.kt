package com.example.preteirb.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.preteirb.ui.AppState
import com.example.preteirb.ui.screens.auth.ChooseAuthenticationDestination
import com.example.preteirb.ui.screens.auth.ChooseAuthenticationScreen
import com.example.preteirb.ui.screens.auth.LoginDestination
import com.example.preteirb.ui.screens.auth.LoginScreen
import com.example.preteirb.ui.screens.auth.SignUpDestination
import com.example.preteirb.ui.screens.auth.SignUpScreen
import com.example.preteirb.ui.screens.booking.BookItemDestination
import com.example.preteirb.ui.screens.booking.BookItemScreen
import com.example.preteirb.ui.screens.items_booked.ItemsBookedDestination
import com.example.preteirb.ui.screens.items_booked.ItemsBookedScreen
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsDestination
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsScreen
import com.example.preteirb.ui.screens.items_owned.ListItemsDestination
import com.example.preteirb.ui.screens.items_owned.ListItemsScreen
import com.example.preteirb.ui.screens.profile_selection.ProfileSelectionDestination
import com.example.preteirb.ui.screens.profile_selection.ProfileSelectionScreen
import com.example.preteirb.ui.screens.search.SearchDestination
import com.example.preteirb.ui.screens.search.SearchScreen

fun NavGraphBuilder.appNavGraph(appState: AppState) {
    composable(route = SearchDestination.route) {
        SearchScreen(
            navigateToBookItem = { appState.navigate("${BookItemDestination.route}/${it}") },
        )
    }

    composable(route = ProfileSelectionDestination.route) {
        ProfileSelectionScreen(
            navigateToSearch = {
                appState.clearAndNavigate(SearchDestination.route)
            },
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

    composable(route = ItemsBookedDestination.route) {
        ItemsBookedScreen()
    }

    composable(route = LoginDestination.route) {
        LoginScreen(
            navigateToSelectProfile = {
                appState.clearAndNavigate(ProfileSelectionDestination.route)
            },
        )
    }

    composable(route = SignUpDestination.route) {
        SignUpScreen(
            navigateToSelectProfile = {
                appState.clearAndNavigate(ProfileSelectionDestination.route)
            },
        )
    }

    composable(route = ChooseAuthenticationDestination.route) {
        ChooseAuthenticationScreen(
            navigateToLogin = {
                appState.navigate(LoginDestination.route)
            },
            navigateToSignUp = {
                appState.navigate(SignUpDestination.route)
            },
        )
    }
}