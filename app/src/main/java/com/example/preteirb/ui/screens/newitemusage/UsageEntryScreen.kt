package com.example.preteirb.ui.screens.newitemusage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.preteirb.model.UsageDetails
import com.example.preteirb.model.UsageEntryViewModel
import com.example.preteirb.model.UsageUiState
import kotlinx.coroutines.launch

@Composable
fun NewUsageScreen(
    modifier: Modifier = Modifier,
    //onValidateForm: () -> Unit = {},
    usageViewModel: UsageEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    itemViewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    NewUsageForm(
        usageUiState = usageViewModel.uiState,
        itemUiState = itemViewModel.itemUiState,
        onUsageValueChange = usageViewModel::updateUiState,
        onItemValueChange = itemViewModel::updateUiState,
        onItemSelectionValueChange = {
            usageViewModel.updateUiState(
                usageViewModel.uiState.usageDetails.copy(
                    itemId = it
                )
            )
        },
        onSaveClick = {
            coroutineScope.launch {
                usageViewModel.saveUsage()
            }
        },
        modifier = modifier
    )
}


@Composable
fun NewUsageForm(
    usageUiState: UsageUiState,
    itemUiState: ItemUiState,
    modifier: Modifier = Modifier,
    onItemSelectionValueChange: (Int) -> Unit = {},
    onItemValueChange: (ItemDetails) -> Unit = {},
    onUsageValueChange: (UsageDetails) -> Unit = {},
    onSaveClick: () -> Unit = {},
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
            objectList = emptyList(),
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
                onClick = onSaveClick,
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
        )
    }
}
