package com.example.preteirb.ui

import androidx.annotation.StringRes
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
import com.example.preteirb.ui.screens.ChooseProfileScreen
import com.example.preteirb.ui.screens.NewUsageScreen
import com.example.preteirb.ui.screens.Profile
import com.example.preteirb.ui.screens.ProfileScreen
import com.example.preteirb.ui.screens.ProfileScreenItems
import com.example.preteirb.ui.screens.SearchScreen


enum class PreteirbScreen(@StringRes val nameRes: Int) {
    Search(R.string.find_object),
    New(R.string.add),
    Account(R.string.account),
    AddProfile(R.string.add_profile),
    SelectProfile(R.string.select_profile),
}

@OptIn(ExperimentalMaterial3Api::class)
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
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PreteirbScreen.Search.name
        ) {
            composable(route = PreteirbScreen.Search.name) {
                SearchScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                )
            }
            
            val fakeProfileScreenItems = listOf(
                ProfileScreenItems(
                    label = R.string.username,
                    icon = R.drawable.ic_broken_image
                ),
                ProfileScreenItems(
                    label = R.string.settings,
                    icon = R.drawable.baseline_settings_24
                ),
                ProfileScreenItems(
                    label = R.string.switch_account,
                    icon = R.drawable.baseline_switch_account_24
                ),
                ProfileScreenItems(
                    label = R.string.logout,
                    icon = R.drawable.baseline_logout_24
                ),
            )
            composable(route = PreteirbScreen.Account.name) {
                ProfileScreen(
                    menuItems = fakeProfileScreenItems,
                    onClickOnMenuItem = {
                        when (it.label) {
                            R.string.username -> {}
                            R.string.settings -> {}
                            R.string.switch_account -> navController.navigate(PreteirbScreen.SelectProfile.name)
                            R.string.logout -> navController.navigate(PreteirbScreen.SelectProfile.name)
                        }
                    },
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
            
            val fakeProfileList = listOf(
                Profile(username = "John"),
                Profile(username = "Jane"),
                Profile(username = "Bob"),
                Profile(username = "Alice"),
            )
            composable(route = PreteirbScreen.SelectProfile.name) {
                ChooseProfileScreen(
                    profileList = fakeProfileList,
                    onClickOnProfile = { navController.navigate(PreteirbScreen.Search.name) }, //TODO: footnote saying welcome username
                    onClickOnAddAccount = { navController.navigate(PreteirbScreen.Search.name) }, //TODO: footnote saying account was created
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