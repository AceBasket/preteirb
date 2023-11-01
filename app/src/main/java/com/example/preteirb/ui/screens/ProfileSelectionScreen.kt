package com.example.preteirb.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.user.User
import com.example.preteirb.model.AppViewModelProvider
import com.example.preteirb.model.ProfileSelectionViewModel
import kotlinx.coroutines.launch


@Composable
fun ProfileSelectionScreen(
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileSelectionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutine = rememberCoroutineScope()
    var isShowAddAccountDialog by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        ProfileList(
            profileList = uiState.users,
            onClickOnProfile = {
                coroutine.launch {
                    viewModel.logIn(it)
                }
                navigateToSearch()
            }
        )
        Row(modifier = Modifier.clickable { isShowAddAccountDialog = true }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_person_add_24),
                contentDescription = null
            )
            Text(text = stringResource(id = R.string.add_account))
        }
        
        if (isShowAddAccountDialog) {
            AddAccountDialog(
                onDismissRequest = { isShowAddAccountDialog = false },
                onAddAccount = { username ->
                    coroutine.launch {
                        viewModel.registerUser(
                            User(
                                userId = 0,
                                username = username,
                                location = "location",
                            )
                        )
                    }
                    isShowAddAccountDialog = false
                }
            )
        }
    }
}

@Composable
fun ProfileList(
    profileList: List<User>,
    onClickOnProfile: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        
        profileList.forEach { profile ->
            Row(
                modifier = Modifier
                    .clickable { onClickOnProfile(profile) }
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_account_circle_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
                Text(text = profile.username)
            }
        }
    }
}

@Composable
fun AddAccountDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onAddAccount: (String) -> Unit = {},
) {
    var username by rememberSaveable { mutableStateOf("") }
    
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(id = R.string.username)) },
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
                TextButton(
                    onClick = { onAddAccount(username) }
                ) {
                    Text(stringResource(id = R.string.add))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileListPreview() {
    AppTheme {
        val fakeProfileList = listOf(
            User(
                userId = 1,
                username = "username1",
                location = "location1",
            ),
            User(
                userId = 2,
                username = "username2",
                location = "location2",
            ),
            User(
                userId = 3,
                username = "username3",
                location = "location3",
            ),
        )
        ProfileList(
            profileList = fakeProfileList,
            onClickOnProfile = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddAccountDialogPreview() {
    AppTheme {
        AddAccountDialog(
            onDismissRequest = { },
            onAddAccount = { }
        )
    }
}
