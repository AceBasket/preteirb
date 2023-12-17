package com.example.preteirb.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.items_owned.ItemUiState

@Composable
fun ItemCreator(
    itemUiState: ItemUiState,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    updateUiState: (ItemDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    ItemDialog(
        itemUiState = itemUiState,
        onValueChange = updateUiState,
        onSaveObject = onConfirmation,
        onDismissDialog = onDismissRequest,
        confirmButtonLabel = R.string.save,
        dialogTitle = R.string.add_object,
        modifier = modifier
    )
}

@Composable
fun ItemEditor(
    itemUiState: ItemUiState,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    updateUiState: (ItemDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    ItemDialog(
        itemUiState = itemUiState,
        onValueChange = updateUiState,
        onSaveObject = onConfirmation,
        onDismissDialog = onDismissRequest,
        confirmButtonLabel = R.string.edit,
        dialogTitle = R.string.edit_object,
        isDisplayDeleteImageButton = true,
        modifier = modifier
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemDialog(
    itemUiState: ItemUiState,
    onValueChange: (ItemDetails) -> Unit,
    onSaveObject: () -> Unit,
    onDismissDialog: () -> Unit,
    @StringRes confirmButtonLabel: Int,
    @StringRes dialogTitle: Int,
    modifier: Modifier = Modifier,
    isDisplayDeleteImageButton: Boolean = false,
) {
    // full screen dialog
    Dialog(
        onDismissRequest = onDismissDialog,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) { // header
                    IconButton(onClick = onDismissDialog) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close_dialog)
                        )
                    }
                    Text(
                        text = stringResource(id = dialogTitle),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    TextButton(
                        onClick = onSaveObject,
                        enabled = itemUiState.isEntryValid
                    ) {
                        Text(text = stringResource(id = confirmButtonLabel))
                    }
                }
                var selectedImageUri by remember {
                    mutableStateOf<Uri?>(null)
                }

                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri ->
                        selectedImageUri = uri
                        if (uri != null) {
                            onValueChange(itemUiState.itemDetails.copy(image = uri))
                        }
                    }
                )
                GlideImage(
                    model = selectedImageUri ?: if (itemUiState.itemDetails.image != Uri.EMPTY)
                        itemUiState.itemDetails.image
                    else
                        R.drawable.baseline_image_24,
                    contentDescription = stringResource(id = R.string.object_photo),
                    loading = placeholder(R.drawable.loading_img),
                    failure = placeholder(R.drawable.baseline_broken_image_24),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size_large))
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)))
                )
                if (isDisplayDeleteImageButton) {
                    OutlinedButton(
                        onClick = { onValueChange(itemUiState.itemDetails.copy(image = Uri.EMPTY)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.remove_image)
                        )
                        Text(text = stringResource(id = R.string.remove_image))
                    }
                }
                var isTitleError by rememberSaveable { mutableStateOf(false) }
                OutlinedTextField(
                    value = itemUiState.itemDetails.name,
                    onValueChange = {
                        onValueChange(itemUiState.itemDetails.copy(name = it))
                        isTitleError = it.isEmpty()
                    },
                    label = { Text(stringResource(id = R.string.title) + '*') },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isTitleError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                )
                OutlinedTextField(
                    value = itemUiState.itemDetails.description,
                    onValueChange = { onValueChange(itemUiState.itemDetails.copy(description = it)) },
                    label = { Text(stringResource(id = R.string.description)) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewObjectDialogPreview() {
    AppTheme {
        ItemDialog(
            itemUiState = ItemUiState(),
            onValueChange = {},
            onSaveObject = {},
            onDismissDialog = {},
            confirmButtonLabel = R.string.save,
            dialogTitle = R.string.add_object,
        )
    }
}