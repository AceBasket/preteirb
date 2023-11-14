package com.example.preteirb.ui.screens.newitemusage

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.preteirb.model.ItemsOwnedUsageEntryViewModel
import com.example.preteirb.model.UsageDetails
import com.example.preteirb.model.UsagePeriod
import com.example.preteirb.model.UsageUiState
import com.example.preteirb.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ItemOwnedUsageEntryDestination : NavigationDestination {
    override val route = "item_owned_usage_entry"
    override val titleRes = R.string.items_owned_new_usages
}

@Composable
fun NewUsageScreen(
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    itemViewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    itemsOwnedUsagesViewModel: ItemsOwnedUsageEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val itemsOwnedUiState by itemsOwnedUsagesViewModel.itemsOwnedUiState.collectAsState()
    NewUsageForm(
        usageUiState = itemsOwnedUsagesViewModel.uiState,
        itemUiState = itemViewModel.itemUiState,
        itemsOwnedUiState = itemsOwnedUiState,
        usagePeriodsCount = itemsOwnedUsagesViewModel.usagePeriodsCount,
        onAddUsagePeriod = itemsOwnedUsagesViewModel::addUsagePeriod,
        onDeleteUsagePeriod = itemsOwnedUsagesViewModel::deleteUsagePeriod,
        onUsageValueChange = itemsOwnedUsagesViewModel::updateUiState,
        onItemValueChange = itemViewModel::updateUiState,
        onItemSelectionValueChange = {
            Log.d("NewUsageScreen", "onItemSelectionValueChange: $it")
            itemsOwnedUsagesViewModel.updateUiState(
                // find item with same name and get id from it
                itemsOwnedUsagesViewModel.uiState.usageDetails.copy(
                    itemId = itemsOwnedUiState.itemsOwned.find { item ->
                        item.name == it
                    }?.itemId ?: 0
                )
            )
            Log.d(
                "NewUsageScreen",
                "onItemSelectionValueChange: ${itemsOwnedUsagesViewModel.uiState.usageDetails}"
            )
        },
        onSaveUsageClick = {
            coroutineScope.launch {
                itemsOwnedUsagesViewModel.saveUsage()
            }
            navigateToHomeScreen()
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
    usagePeriodsCount: List<Int>,
    onAddUsagePeriod: () -> Unit,
    onDeleteUsagePeriod: (Int) -> Unit,
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
        ExposedDropdownObjectSelection(
            objectList = itemsOwnedUiState.itemsOwned,
            onValueChange = onItemSelectionValueChange,
            onAddItem = { isShowObjectDialog = true },
        )
        AddUsages(
            usageUiState = usageUiState,
            usagePeriodsCount = usagePeriodsCount,
            onAddUsagePeriod = onAddUsagePeriod,
            onDeleteUsagePeriod = onDeleteUsagePeriod,
            onUsageValueChange = onUsageValueChange,
            onSaveUsageClick = onSaveUsageClick,
        )
        
        /* repeat(usagesCount) { index ->
            NewUsagePeriod(
                onAddUsagePeriod = { usagesCount++ },
                onNewUsagePeriodSelected = {
                    if (it.first != null && it.second != null) {
                        val periodAdded = usageUiState.usageDetails.period
                        periodAdded.add(index, UsagePeriod(it.first!!, it.second!!))
                        
                        onUsageValueChange(
                            usageUiState.usageDetails.copy(
                                period = periodAdded
                            )
                        )
                    }
                },
                isLastUsagePeriodEntry = index == usagesCount - 1,
                onUpdateUsagePeriod = {
                    if (it.first != null && it.second != null) {
                        val periodUpdated = usageUiState.usageDetails.period
                        periodUpdated[index] = UsagePeriod(it.first!!, it.second!!)
                        
                        onUsageValueChange(
                            usageUiState.usageDetails.copy(
                                period = periodUpdated
                            )
                        )
                    }
                },
                onDeleteUsagePeriod = {
                    val periodRemoved = usageUiState.usageDetails.period
                    periodRemoved.removeAt(index)
                    onUsageValueChange(
                        usageUiState.usageDetails.copy(
                            period = periodRemoved
                        )
                    )
                    usagesCount--
                },
            )
        } */
        
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

@Composable
fun AddUsages(
    usageUiState: UsageUiState,
    usagePeriodsCount: List<Int>,
    onAddUsagePeriod: () -> Unit,
    onDeleteUsagePeriod: (Int) -> Unit,
    onUsageValueChange: (UsageDetails) -> Unit,
    onSaveUsageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(usagePeriodsCount) { index, usagePeriodId ->
            NewUsagePeriod(
                onNewUsagePeriodSelected = {
                    if (it.first != null && it.second != null) {
                        val periodAdded = usageUiState.usageDetails.period
                        periodAdded.add(UsagePeriod(it.first!!, it.second!!))
                        
                        onUsageValueChange(
                            usageUiState.usageDetails.copy(
                                period = periodAdded
                            )
                        )
                    }
                },
                onAddUsagePeriod = {
                    onAddUsagePeriod()
                },
                onDeleteUsagePeriod = {
                    val periodRemoved = usageUiState.usageDetails.period
                    periodRemoved.removeAt(index)
                    onUsageValueChange(
                        usageUiState.usageDetails.copy(
                            period = periodRemoved
                        )
                    )
                    onDeleteUsagePeriod(usagePeriodId)
                },
                onUpdateUsagePeriod = {
                    if (it.first != null && it.second != null) {
                        val periodUpdated = usageUiState.usageDetails.period
                        periodUpdated[index] = UsagePeriod(it.first!!, it.second!!)
                        
                        onUsageValueChange(
                            usageUiState.usageDetails.copy(
                                period = periodUpdated
                            )
                        )
                    }
                },
                isLastUsagePeriodEntry = index == usagePeriodsCount.lastIndex,
                notSelectablePeriods = usageUiState.bookedPeriods,
            )
        }
    }
    
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        Button(
            onClick = onSaveUsageClick,
            enabled = usageUiState.isEntryValid,
        ) {
            Text(text = stringResource(id = R.string.post))
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
            usagePeriodsCount = listOf(0),
            onItemSelectionValueChange = {},
            onItemValueChange = {},
            onUsageValueChange = {},
            onSaveUsageClick = {},
            onSaveItem = {},
            onAddUsagePeriod = {},
            onDeleteUsagePeriod = {},
        )
    }
}
