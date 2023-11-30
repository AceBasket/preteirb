package com.example.preteirb.ui.screens.items_owned

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
                OutlinedTextField(
                    value = itemUiState.itemDetails.name,
                    onValueChange = { onValueChange(itemUiState.itemDetails.copy(name = it)) },
                    label = { Text(stringResource(id = R.string.title)) },
                    modifier = Modifier.fillMaxWidth()
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