package com.example.preteirb.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.compose.AppTheme
import com.example.preteirb.R

data class Profile(
    val username: String,
)

@Composable
fun ChooseProfileScreen(
    profileList: List<Profile>,
    modifier: Modifier = Modifier,
    onClickOnProfile: (Profile) -> Unit = {},
    onClickOnAddAccount: () -> Unit = {},
    ) {
    Column(
        modifier = modifier
    ) {
        profileList.forEach { profile ->
            Box(modifier = Modifier.clickable { onClickOnProfile(profile) }) {
                IconAndLabelCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_account_circle_24),
                            contentDescription = null
                        )
                    },
                    label = profile.username,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
        Box(modifier = Modifier.clickable { onClickOnAddAccount() }) {
            IconAndLabelCard(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person_add_24),
                        contentDescription = null
                    )
                },
                label = stringResource(id = R.string.add_account)
            )
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
        onDismissRequest = { /*TODO*/ }
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
                    onClick = { /*TODO*/ }
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
                TextButton(
                    onClick = { /*TODO*/ }
                ) {
                    Text(stringResource(id = R.string.add))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseProfileScreenPreview() {
    AppTheme {
        val fakeProfileList = listOf(
            Profile(username = "John"),
            Profile(username = "Jane"),
            Profile(username = "Bob"),
            Profile(username = "Alice"),
        )
        ChooseProfileScreen(
            profileList = fakeProfileList,
            onClickOnProfile = {  },
            onClickOnAddAccount = {  }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddAccountDialogPreview() {
    AppTheme {
        AddAccountDialog(
            onDismissRequest = {  },
            onAddAccount = {  }
        )
    }
}
