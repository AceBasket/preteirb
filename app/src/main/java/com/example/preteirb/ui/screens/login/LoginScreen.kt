package com.example.preteirb.ui.screens.login

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.login.LogInViewModel
import com.example.preteirb.model.login.LoginDetails
import com.example.preteirb.model.login.LoginState
import com.example.preteirb.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object LoginDestination : NavigationDestination {
    override val route: String = "login"
    override val titleRes: Int = R.string.login
}

@Composable
fun LoginScreen(
    navigateToSelectProfile: () -> Unit,
    modifier: Modifier = Modifier,
    logInViewModel: LogInViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    LoginScreenContent(
        uiState = logInViewModel.uiState,
        updateUiState = logInViewModel::updateUiState,
        login = {
            coroutineScope.launch {
                logInViewModel.login()
                navigateToSelectProfile()
            }
        },
        modifier = modifier
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginState,
    updateUiState: (LoginDetails) -> Unit,
    login: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginForm(
        loginState = uiState,
        updateUiState = updateUiState,
        login = login,
        isAccountCreation = uiState.isAccountCreation,
        buttonLabel = R.string.login,
        modifier = modifier
    )
}

@Composable
fun LoginForm(
    loginState: LoginState,
    updateUiState: (LoginDetails) -> Unit,
    login: () -> Unit,
    isAccountCreation: Boolean,
    @StringRes buttonLabel: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = loginState.loginDetails.username,
            onValueChange = {
                updateUiState(loginState.loginDetails.copy(username = it))
            },
            label = { Text(text = stringResource(R.string.username)) }
        )
        OutlinedTextField(
            value = loginState.loginDetails.password,
            onValueChange = {
                updateUiState(loginState.loginDetails.copy(password = it))
            },
            label = { Text(text = stringResource(R.string.password)) }
        )
        if (isAccountCreation) {
            OutlinedTextField(
                value = loginState.loginDetails.passwordConfirmation,
                onValueChange = {
                    updateUiState(loginState.loginDetails.copy(passwordConfirmation = it))
                },
                label = { Text(text = stringResource(R.string.confirm_password)) }
            )
        }
        Button(
            onClick = login,
            enabled = loginState.isEntryValid
        ) {
            Text(text = stringResource(id = buttonLabel))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreenContent(
            uiState = LoginState(),
            updateUiState = {},
            login = {},
        )
    }
}