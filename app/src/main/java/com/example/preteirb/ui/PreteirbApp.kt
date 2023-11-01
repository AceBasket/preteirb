package com.example.preteirb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.ui.screens.ProfileScreen
import com.example.preteirb.ui.screens.ProfileSelectionScreen
import com.example.preteirb.ui.screens.SearchScreen
import com.example.preteirb.ui.screens.newitemusage.NewUsageScreen


enum class PreteirbScreen(@StringRes val nameRes: Int) {
    Search(R.string.find_object),
    New(R.string.add),
    Account(R.string.account),
    AddProfile(R.string.add_profile),
    SelectProfile(R.string.select_profile),
}

@Composable
fun PreteirbApp(modifier: Modifier = Modifier) {
    //Create NavController
    val navController = rememberNavController()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = PreteirbScreen.valueOf(
        backStackEntry?.destination?.route ?: PreteirbScreen.Search.name
    )
    
    Scaffold(
        modifier = modifier,
        topBar = {
            LoanAppTopBar(
                currentScreenTitle = currentScreen.nameRes,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            BottomLoanAppBar(
                navController = navController,
                backStackEntry = null,
                isDisplayBottomAppBar = navController.currentDestination?.route != PreteirbScreen.SelectProfile.name,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PreteirbScreen.SelectProfile.name
        ) {
            composable(route = PreteirbScreen.Search.name) {
                SearchScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            
            composable(route = PreteirbScreen.Account.name) {
                ProfileScreen(
                    navigateToSelectProfile = { navController.navigate(PreteirbScreen.SelectProfile.name) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            composable(route = PreteirbScreen.New.name) {
                NewUsageScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            composable(route = PreteirbScreen.SelectProfile.name) {
                ProfileSelectionScreen(
                    navigateToSearch = {
                        // pop back stack till and including the given route
                        navController.popBackStack(PreteirbScreen.SelectProfile.name, true)
                        navController.navigate(PreteirbScreen.Search.name)
                    }, //TODO: footnote saying account was created
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
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