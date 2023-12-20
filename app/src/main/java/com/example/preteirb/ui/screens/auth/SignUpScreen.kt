package com.example.preteirb.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.auth.AuthDetails
import com.example.preteirb.model.auth.AuthState
import com.example.preteirb.model.auth.AuthViewModel
import com.example.preteirb.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object SignUpDestination : NavigationDestination {
    override val route: String = "sign_up"
    override val titleRes: Int = R.string.sign_up
}

@Composable
fun SignUpScreen(
    navigateToSelectProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.setAccountCreation(true)

    SignUpScreenContent(
        uiState = viewModel.uiState,
        updateUiState = viewModel::updateUiState,
        signUp = {
            coroutineScope.launch {
                viewModel.signUp()
                if (viewModel.uiState.isAuthSuccess) {
                    navigateToSelectProfile()
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun SignUpScreenContent(
    uiState: AuthState,
    updateUiState: (AuthDetails) -> Unit,
    signUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginForm(
        authState = uiState,
        updateUiState = updateUiState,
        login = signUp,
        isAccountCreation = true,
        buttonLabel = R.string.sign_up,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenContentPreview() {
    AppTheme {
        SignUpScreenContent(
            uiState = AuthState(),
            updateUiState = {},
            signUp = {},
        )
    }
}