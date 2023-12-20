package com.example.preteirb.ui.screens.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.auth.AuthDetails
import com.example.preteirb.model.auth.AuthState
import com.example.preteirb.model.auth.AuthViewModel
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
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    LoginScreenContent(
        uiState = authViewModel.uiState,
        updateUiState = authViewModel::updateUiState,
        login = {
            coroutineScope.launch {
                authViewModel.login()
                if (authViewModel.uiState.isAuthSuccess) {
                    navigateToSelectProfile()
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun LoginScreenContent(
    uiState: AuthState,
    updateUiState: (AuthDetails) -> Unit,
    login: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginForm(
        authState = uiState,
        updateUiState = updateUiState,
        login = login,
        isAccountCreation = uiState.isAccountCreation,
        buttonLabel = R.string.login,
        modifier = modifier
    )
}

@Composable
fun LoginForm(
    authState: AuthState,
    updateUiState: (AuthDetails) -> Unit,
    login: () -> Unit,
    isAccountCreation: Boolean,
    @StringRes buttonLabel: Int,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        TextField(
            value = authState.authDetails.username,
            onValueChange = {
                updateUiState(authState.authDetails.copy(username = it.trimEnd()))
            },
            label = { Text(text = stringResource(R.string.username)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.username)
                )
            },
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_extra_small))
        )
        var isPassword1Visible by remember { mutableStateOf(false) }
        TextField(
            value = authState.authDetails.password,
            onValueChange = {
                updateUiState(authState.authDetails.copy(password = it))
            },
            label = { Text(text = stringResource(R.string.password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.password)
                )
            },
            trailingIcon = {
                IconButton(onClick = { isPassword1Visible = !isPassword1Visible }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = if (isPassword1Visible) R.drawable.baseline_visibility_24
                            else R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = null,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!isAccountCreation) {
                        login()
                    }
                }
            ),
            visualTransformation = if (isPassword1Visible)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_extra_small))
        )
        if (isAccountCreation) {
            var isPassword2Visible by remember { mutableStateOf(false) }
            TextField(
                value = authState.authDetails.passwordConfirmation,
                onValueChange = {
                    updateUiState(authState.authDetails.copy(passwordConfirmation = it))
                },
                label = { Text(text = stringResource(R.string.confirm_password)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.password)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPassword2Visible = !isPassword2Visible }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = if (isPassword2Visible) R.drawable.baseline_visibility_24
                                else R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = null,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = { login() }
                ),
                visualTransformation = if (isPassword2Visible) VisualTransformation.None else PasswordVisualTransformation()
            )
        }
        Button(
            onClick = {
                login()
                focusManager.clearFocus()
            },
            enabled = authState.isEntryValid
        ) {
            Text(text = stringResource(id = buttonLabel))
        }

        if (authState.isAuthLoading) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreenContent(
            uiState = AuthState(),
            updateUiState = {},
            login = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    AppTheme {
        SignUpScreenContent(
            uiState = AuthState(),
            updateUiState = {},
            signUp = {},
        )
    }
}