package com.example.preteirb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.ui.screens.items_booked.ItemsBookedDestination
import com.example.preteirb.ui.screens.items_owned.ListItemsDestination
import com.example.preteirb.ui.screens.search.SearchDestination

enum class TopLevelDestination(
    @StringRes val titleRes: Int,
    val route: String,
    val icon: ImageVector,
) {
    SEARCH(
        titleRes = SearchDestination.titleRes,
        route = SearchDestination.route,
        icon = Icons.Rounded.Search,
    ),
    BOOKINGS(
        titleRes = ItemsBookedDestination.titleRes,
        route = ItemsBookedDestination.route,
        icon = Icons.Rounded.DateRange,
    ),
    LIST_ITEMS(
        titleRes = ListItemsDestination.titleRes,
        route = ListItemsDestination.route,
        icon = Icons.AutoMirrored.Rounded.List,
    ),
}

@Composable
fun BottomAppBar(
    destinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    isDisplayBottomAppBar: Boolean = true,
) {
    if (!isDisplayBottomAppBar) return
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination.isTopLevelDestinationInHierarchy(destination),
                onClick = { onNavigateToDestination(destination) },
                label = {
                    Text(
                        text = stringResource(id = destination.titleRes),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = "${stringResource(destination.titleRes)} Icon",
                    )
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.equals(destination.route, true) ?: false
    } ?: false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToProfileSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            Box {
                IconButton(onClick = { isExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null,
                    )
                }
                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.logout))
                        },
                        onClick = {
                            isExpanded = false
                            navigateToProfileSelection()
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_logout_24),
                                contentDescription = stringResource(id = R.string.logout_icon)
                            )
                        }
                    )
                }
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
fun BottomAppBarPreview() {
    AppTheme {
        BottomAppBar(
            destinations = TopLevelDestination.values().asList(),
            currentDestination = NavDestination("search"),
            onNavigateToDestination = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    AppTheme {
        AppTopBar(
            currentScreenTitle = R.string.app_name,
            canNavigateBack = false,
            navigateUp = {},
            navigateToProfileSelection = {},
        )
    }
}