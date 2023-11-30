package com.example.preteirb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.ui.screens.items_owned.ListItemsDestination
import com.example.preteirb.ui.screens.search.SearchDestination

data class BottomNavItem(
    @StringRes val nameRes: Int,
    val route: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        nameRes = SearchDestination.titleRes,
        route = SearchDestination.route,
        icon = Icons.Rounded.Search,
    ),
//    BottomNavItem(
//        nameRes = ItemOwnedUsageEntryDestination.titleRes,
//        route = ItemOwnedUsageEntryDestination.route,
//        icon = Icons.Rounded.AddCircle,
//    ),
    BottomNavItem(
        nameRes = ListItemsDestination.titleRes,
        route = ListItemsDestination.route,
        icon = Icons.Rounded.List,
    ),
)


@Composable
fun BottomAppBar(
    navController: NavController,
    backStackEntry: NavBackStackEntry?,
    modifier: Modifier = Modifier,
    isDisplayBottomAppBar: Boolean = true,
) {
    if (!isDisplayBottomAppBar) return
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = item.route == backStackEntry?.destination?.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { navController.navigate(item.route) },
                label = {
                    Text(
                        text = stringResource(id = item.nameRes),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${stringResource(item.nameRes)} Icon",
                    )
                }
            )
        }
    }
}

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
                                contentDescription = "Logout icon"
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
            navController = rememberNavController(),
            backStackEntry = null
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