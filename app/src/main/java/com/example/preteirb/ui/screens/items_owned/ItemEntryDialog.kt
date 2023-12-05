package com.example.preteirb.ui.screens.items_owned

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.items_owned.ItemUiState

@Composable
fun NewObjectDialog(
    itemUiState: ItemUiState,
    onValueChange: (ItemDetails) -> Unit,
    onSaveObject: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
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
                    Text(text = stringResource(id = R.string.add_object))
                    TextButton(onClick = onSaveObject) {
                        Text(text = stringResource(id = R.string.save))
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
        NewObjectDialog(
            itemUiState = ItemUiState(),
            onValueChange = {},
            onSaveObject = {},
            onDismissDialog = {},
        )
    }
}