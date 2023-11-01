package com.example.preteirb.ui.screens.newitemusage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.AppViewModelProvider
import com.example.preteirb.model.ItemDetails
import com.example.preteirb.model.ItemEntryViewModel
import com.example.preteirb.model.ItemUiState
import com.example.preteirb.model.ItemsOwnedUiState
import com.example.preteirb.model.UsageDetails
import com.example.preteirb.model.UsageEntryViewModel
import com.example.preteirb.model.UsageUiState
import kotlinx.coroutines.launch

@Composable
fun NewUsageScreen(
    modifier: Modifier = Modifier,
    usageViewModel: UsageEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    itemViewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val itemsOwnedUiState by usageViewModel.itemsOwnedUiState.collectAsState()
    NewUsageForm(
        usageUiState = usageViewModel.uiState,
        itemUiState = itemViewModel.itemUiState,
        itemsOwnedUiState = itemsOwnedUiState,
        onUsageValueChange = usageViewModel::updateUiState,
        onItemValueChange = itemViewModel::updateUiState,
        onItemSelectionValueChange = {
            usageViewModel.updateUiState(
                // find item with same name and get id from it
                usageViewModel.uiState.usageDetails.copy(
                    itemId = itemsOwnedUiState.itemsOwned.find { item ->
                        item.name == it
                    }?.itemId ?: 0
                )
            )
        },
        onSaveUsageClick = {
            coroutineScope.launch {
                usageViewModel.saveUsage()
            }
        },
        onSaveItem = {
            coroutineScope.launch {
                itemViewModel.saveItem()
            }
        },
        modifier = modifier
    )
}


@Composable
fun NewUsageForm(
    usageUiState: UsageUiState,
    itemUiState: ItemUiState,
    itemsOwnedUiState: ItemsOwnedUiState,
    onItemSelectionValueChange: (String) -> Unit,
    onItemValueChange: (ItemDetails) -> Unit,
    onUsageValueChange: (UsageDetails) -> Unit,
    onSaveUsageClick: () -> Unit,
    onSaveItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var usagesCount by rememberSaveable {
        mutableIntStateOf(1)
    }
    
    var isShowObjectDialog by rememberSaveable {
        mutableStateOf(false)
    }
    
    
    Column(
        modifier = modifier
    ) {
        ObjectSelection(
            objectList = itemsOwnedUiState.itemsOwned,
            onValueChange = onItemSelectionValueChange,
            onAddItem = { isShowObjectDialog = true },
        )
        repeat(usagesCount) {
            NewUsagePeriod(
                onAddUsagePeriod = {
                    if (it.first != null && it.second != null) {
                        usagesCount++
                        onUsageValueChange(
                            usageUiState.usageDetails.copy(
                                period = usageUiState.usageDetails.period + Pair(
                                    it.first!!,
                                    it.second!!
                                )
                            )
                        )
                    }
                },
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.padding_small))
        ) {
            Button(
                onClick = onSaveUsageClick,
                enabled = usageUiState.isEntryValid && itemUiState.isEntryValid,
            ) {
                Text(text = stringResource(id = R.string.post))
            }
        }
        
        if (isShowObjectDialog) {
            NewObjectDialog(
                itemUiState = itemUiState,
                onValueChange = onItemValueChange,
                onSaveObject = {
                    onSaveItem()
                    isShowObjectDialog = false
                },
                onDismissDialog = {
                    isShowObjectDialog = false
                },
            )
        }
        
        
    }
}


@Preview(showBackground = true)
@Composable
fun NewUsageFormPreview() {
    AppTheme {
        NewUsageForm(
            usageUiState = UsageUiState(),
            itemUiState = ItemUiState(),
            itemsOwnedUiState = ItemsOwnedUiState(),
            onItemSelectionValueChange = {},
            onItemValueChange = {},
            onUsageValueChange = {},
            onSaveUsageClick = {},
            onSaveItem = {},
        )
    }
}
