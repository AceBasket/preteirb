package com.example.preteirb.ui

/*
* Heavily inspired by the NowInAndroid app: https://github.com/android/nowinandroid
* and the MakeItSo app: https://github.com/FirebaseExtended/make-it-so-android
*/

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.preteirb.common.snackbar.SnackbarManager
import com.example.preteirb.common.snackbar.SnackbarMessage.Companion.toMessage
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.screens.auth.ChooseAuthenticationDestination
import com.example.preteirb.ui.screens.auth.LoginDestination
import com.example.preteirb.ui.screens.auth.SignUpDestination
import com.example.preteirb.ui.screens.booking.BookItemDestination
import com.example.preteirb.ui.screens.items_booked.ItemsBookedDestination
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsDestination
import com.example.preteirb.ui.screens.items_owned.ListItemsDestination
import com.example.preteirb.ui.screens.profile_selection.ProfileSelectionDestination
import com.example.preteirb.ui.screens.search.SearchDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class AppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                snackbarHostState.showSnackbar(text)
                snackbarManager.clearSnackbarState()
            }
        }
    }

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentScreen: NavigationDestination
        @Composable get() = currentDestination?.route.let { route ->
            when (route) {
                SearchDestination.route -> SearchDestination
                ProfileSelectionDestination.route -> ProfileSelectionDestination
                BookItemDestination.routeWithArgs -> BookItemDestination
                ListItemsDestination.route -> ListItemsDestination
                ItemAndUsagesDetailsDestination.routeWithArgs -> ItemAndUsagesDetailsDestination
                ItemsBookedDestination.route -> ItemsBookedDestination
                LoginDestination.route -> LoginDestination
                SignUpDestination.route -> SignUpDestination
                ChooseAuthenticationDestination.route -> ChooseAuthenticationDestination
                else -> null
            }
        } ?: ChooseAuthenticationDestination

    val displayProfileIcon: Boolean
        @Composable get() = currentDestination?.route != ProfileSelectionDestination.route
                && currentDestination?.route != LoginDestination.route
                && currentDestination?.route != SignUpDestination.route
                && currentDestination?.route != ChooseAuthenticationDestination.route

    val displayBottomAppBar: Boolean
        @Composable get() = displayProfileIcon

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            TopLevelDestination.SEARCH.route -> TopLevelDestination.SEARCH
            TopLevelDestination.LIST_ITEMS.route -> TopLevelDestination.LIST_ITEMS
            TopLevelDestination.BOOKINGS.route -> TopLevelDestination.BOOKINGS
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.SEARCH -> {
                navController.navigate(
                    route = topLevelDestination.route,
                    navOptions = topLevelNavOptions
                )
            }

            TopLevelDestination.LIST_ITEMS -> {
                navController.navigate(
                    route = topLevelDestination.route,
                    navOptions = topLevelNavOptions
                )
            }

            TopLevelDestination.BOOKINGS -> {
                navController.navigate(
                    route = topLevelDestination.route,
                    navOptions = topLevelNavOptions
                )
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        AppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
    }