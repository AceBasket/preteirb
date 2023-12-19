package com.example.preteirb.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.ProfileDetails
import com.example.preteirb.model.ProfileUiState

@Composable
fun ProfileEditor(
    uiState: ProfileUiState,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    updateUiState: (ProfileDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileDialog(
        uiState = uiState,
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        updateUiState = updateUiState,
        confirmButtonLabel = R.string.edit,
        dialogTitle = R.string.edit_profile,
        modifier = modifier,
    )
}

@Composable
fun ProfileCreator(
    uiState: ProfileUiState,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    updateUiState: (ProfileDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileDialog(
        uiState = uiState,
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        updateUiState = updateUiState,
        confirmButtonLabel = R.string.create,
        dialogTitle = R.string.create_profile,
        modifier = modifier,
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileDialog(
    uiState: ProfileUiState,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    updateUiState: (ProfileDetails) -> Unit,
    @StringRes confirmButtonLabel: Int,
    @StringRes dialogTitle: Int,
    modifier: Modifier = Modifier,
) {
    var username by remember { mutableStateOf(uiState.profileDetails.username) }
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            shape = RoundedCornerShape(16.dp),
        ) {
            var selectedImageUri by remember {
                mutableStateOf<Uri?>(null)
            }

            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    selectedImageUri = uri
                    if (uri != null) {
                        updateUiState(uiState.profileDetails.copy(profilePicture = uri))
                    }
                }
            )
            Text(
                text = stringResource(id = dialogTitle),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            GlideImage(
                model = selectedImageUri ?: if (uiState.profileDetails.profilePicture != Uri.EMPTY)
                    uiState.profileDetails.profilePicture
                else
                    Icons.Default.AccountCircle,
                contentDescription = uiState.profileDetails.username,
                loading = placeholder(R.drawable.loading_img),
                failure = placeholder(rememberVectorPainter(Icons.Default.AccountCircle)),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.image_size_large))
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .clip(CircleShape),
            )
            var isUsernameError by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                value = username,
                onValueChange = { newUsername ->
                    username = newUsername.trimStart { it == ' ' }
                    isUsernameError = username.isEmpty()
                    updateUiState(uiState.profileDetails.copy(username = username))
                },
                label = { Text(text = stringResource(id = R.string.username) + '*') },
                singleLine = true,
                isError = isUsernameError,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    if (uiState.isEntryValid) {
                        onConfirmation()
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                    )
                }
                TextButton(onClick = onConfirmation, enabled = uiState.isEntryValid) {
                    Text(
                        text = stringResource(id = confirmButtonLabel),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditerPreview() {
    AppTheme {
        ProfileEditor(
            uiState = ProfileUiState(
                profileDetails = ProfileDetails(),
            ),
            onDismissRequest = {},
            onConfirmation = {},
            updateUiState = {},
        )
    }
}