package com.example.preteirb.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.preteirb.ui.screens.PostObjectScreen
import com.example.preteirb.ui.screens.ProfileScreen
import com.example.preteirb.ui.screens.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreteirbApp(modifier: Modifier = Modifier) {
    //Create NavController
    val navController = rememberNavController()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = LoanScreen.valueOf(
        backStackEntry?.destination?.route ?: LoanScreen.Search.name
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
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoanScreen.Search.name
        ) {
            composable(route = LoanScreen.Search.name) {
                SearchScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            composable(route = LoanScreen.Account.name) {
                ProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            composable(route = LoanScreen.Add.name) {
                PostObjectScreen(
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
fun PreteibAppPreview() {
    AppTheme {
        PreteirbApp()
    }
}