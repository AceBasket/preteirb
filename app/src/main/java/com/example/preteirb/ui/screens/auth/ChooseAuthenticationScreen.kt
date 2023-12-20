package com.example.preteirb.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.ui.navigation.NavigationDestination

object ChooseAuthenticationDestination : NavigationDestination {
    override val route: String = "choose_authentication"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun ChooseAuthenticationScreen(
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Button(onClick = navigateToSignUp, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.sign_up))
        }
        OutlinedButton(onClick = navigateToLogin, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseAuthenticationScreenPreview() {
    AppTheme {
        ChooseAuthenticationScreen(
            navigateToLogin = {},
            navigateToSignUp = {},
        )
    }
}