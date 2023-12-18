package com.example.preteirb.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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
        loginState = logInViewModel.loginState,
        updateUiState = logInViewModel::updateUiState,
        login = {
            coroutineScope.launch {
                logInViewModel.login()
            }
            navigateToSelectProfile()
        },
        modifier = modifier
    )
}

@Composable
fun LoginScreenContent(
    loginState: LoginState,
    updateUiState: (LoginDetails) -> Unit,
    login: () -> Unit,
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
            }
        )
        OutlinedTextField(
            value = loginState.loginDetails.password,
            onValueChange = {
                updateUiState(loginState.loginDetails.copy(password = it))
            }
        )
        Button(onClick = login) {
            Text(text = "Log In")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        loginState = LoginState(),
        updateUiState = {},
        login = {}
    )
}