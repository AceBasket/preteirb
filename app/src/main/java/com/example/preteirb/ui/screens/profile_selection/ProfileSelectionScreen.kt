package com.example.preteirb.ui.screens.profile_selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.user.User
import com.example.preteirb.model.profile_selection.ProfileSelectionUiState
import com.example.preteirb.model.profile_selection.ProfileSelectionViewModel
import com.example.preteirb.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ProfileSelectionDestination : NavigationDestination {
    override val route = "profile_selection"
    override val titleRes = R.string.select_profile
}

@Composable
fun ProfileSelectionScreen(
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutine = rememberCoroutineScope()



    ProfileSelection(
        uiState = uiState,
        onAddAccount = { username ->
            coroutine.launch {
                viewModel.registerUserAndLogIn(
                    User(
                        id = 0,
                        username = username,
                        profilePicture = "",
                    )
                )
            }
            navigateToSearch()
        },
        onClickOnProfile = {
            coroutine.launch {
                viewModel.logIn(it)
            }
            navigateToSearch()
        },
        modifier = modifier
    )
}

@Composable
fun ProfileSelection(
    uiState: ProfileSelectionUiState,
    onAddAccount: (String) -> Unit,
    onClickOnProfile: (User) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isShowAddAccountDialog by rememberSaveable { mutableStateOf(false) }

    ProfileSelectionCarousel(
        list = uiState.users,
        onClickOnProfile = onClickOnProfile,
        onClickOnAddProfile = { isShowAddAccountDialog = true },
        modifier = modifier
    )

    if (isShowAddAccountDialog) {
        AddAccountDialog(
            onDismissRequest = { isShowAddAccountDialog = false },
            onAddAccount = {
                onAddAccount(it)
                isShowAddAccountDialog = false
            },
            modifier = Modifier.testTag("addProfileDialog")
        )
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
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_medium)),
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var isUsernameError by rememberSaveable { mutableStateOf(false) }
                OutlinedTextField(
                    value = username,
                    onValueChange = { newUsername ->
                        username = newUsername.trimStart { it == ' ' }
                        isUsernameError = username.isEmpty()
                    },
                    label = { Text(stringResource(id = R.string.username) + '*') },
                    singleLine = true,
                    isError = isUsernameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        onAddAccount(username)
                    }
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
}

@Preview(showBackground = true)
@Composable
fun ProfileSelectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val fakeProfileList = listOf(
                User(
                    id = 1,
                    username = "username1",
                    profilePicture = "",
                ),
                User(
                    id = 2,
                    username = "username2",
                    profilePicture = "",
                ),
                User(
                    id = 3,
                    username = "username3",
                    profilePicture = "",
                ),
            )
            ProfileSelection(
                uiState = ProfileSelectionUiState(
                    users = fakeProfileList
                ),
                onAddAccount = { },
                onClickOnProfile = { }
            )
        }
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
