package com.example.preteirb.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.login.LogInViewModel
import com.example.preteirb.model.login.LoginDetails
import com.example.preteirb.model.login.LoginState
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
    viewModel: LogInViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.setAccountCreation(true)

    SignUpScreenContent(
        uiState = viewModel.uiState,
        updateUiState = viewModel::updateUiState,
        signUp = {
            coroutineScope.launch {
                viewModel.signUp()
                navigateToSelectProfile()
            }
        },
        modifier = modifier
    )
}

@Composable
fun SignUpScreenContent(
    uiState: LoginState,
    updateUiState: (LoginDetails) -> Unit,
    signUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginForm(
        loginState = uiState,
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
            uiState = LoginState(),
            updateUiState = {},
            signUp = {},
        )
    }
}