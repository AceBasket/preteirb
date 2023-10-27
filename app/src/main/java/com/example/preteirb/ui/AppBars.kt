package com.example.preteirb.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.preteirb.R

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        name = PreteirbScreen.Search.name,
        route = PreteirbScreen.Search.name,
        icon = Icons.Rounded.Search,
    ),
    BottomNavItem(
        name = PreteirbScreen.New.name,
        route = PreteirbScreen.New.name,
        icon = Icons.Rounded.AddCircle,
    ),
    BottomNavItem(
        name = PreteirbScreen.Account.name,
        route = PreteirbScreen.Account.name,
        icon = Icons.Rounded.AccountCircle,
    ),
)


@Composable
fun BottomLoanAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    backStackEntry: NavBackStackEntry?
) {
    
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
                        text = item.name,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.name} Icon",
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanAppTopBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BottomAppBarPreview() {
    AppTheme {
        BottomLoanAppBar(
            navController = rememberNavController(),
            backStackEntry = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    AppTheme {
        LoanAppTopBar(
            currentScreenTitle = R.string.app_name,
            canNavigateBack = false,
            navigateUp = {},
        )
    }
}