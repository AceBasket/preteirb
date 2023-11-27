package com.example.preteirb.ui.screens.newitemusage

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
import com.example.preteirb.ui.screens.list.NewObjectDialog
import kotlinx.coroutines.launch

object ItemOwnedUsageEntryDestination : NavigationDestination {
    override val route = "item_owned_usage_entry"
    override val titleRes = R.string.items_owned_new_usages
}

@Composable
fun NewUsageScreen(
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    //itemViewModel: ItemEntryViewModel = hiltViewModel(),
    itemViewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    //itemsOwnedUsagesViewModel: ItemsOwnedUsageEntryViewModel = hiltViewModel(),x
    itemsOwnedUsagesViewModel: ItemsOwnedUsageEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val itemsOwnedUiState by itemsOwnedUsagesViewModel.itemsOwnedUiState.collectAsState()
    NewUsageForm(
        usageUiState = itemsOwnedUsagesViewModel.uiState,
        itemUiState = itemViewModel.itemUiState,
        itemsOwnedUiState = itemsOwnedUiState,
        onUsageValueChange = itemsOwnedUsagesViewModel::updateUiState,
        onItemValueChange = itemViewModel::updateUiState,
        onItemSelectionValueChange = {
            itemsOwnedUsagesViewModel.updateUiState(
                // find item with same name and get id from it
                itemsOwnedUsagesViewModel.uiState.usageDetails.copy(
                    itemId = itemsOwnedUiState.itemsOwned.find { item ->
                        item.name == it
                    }?.itemId ?: 0
                )
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
    onItemSelectionValueChange: (String) -> Unit,
    onItemValueChange: (ItemDetails) -> Unit,
    onUsageValueChange: (UsageDetails) -> Unit,
    onSaveUsageClick: () -> Unit,
    onSaveItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        AddUsagesV2(
            usageUiState = usageUiState,
            onUsageValueChange = onUsageValueChange,
            onSaveUsageClick = onSaveUsageClick,
        )

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
fun AddUsagesV2(
    usageUiState: UsageUiState,
    onUsageValueChange: (UsageDetails) -> Unit,
    onSaveUsageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = usageUiState.usageDetails.period,
        ) { index, item ->
            UsagePeriodListItem(
                onNewUsagePeriodSelected = {
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
                notSelectablePeriods = usageUiState.bookedPeriods + usageUiState.usageDetails.period,
                usagePeriod = item,
                onDeleteUsagePeriod = {
                    val periodRemoved = usageUiState.usageDetails.period
                    periodRemoved.removeAt(index)
                    onUsageValueChange(
                        usageUiState.usageDetails.copy(
                            period = periodRemoved
                        )
                    )
                    //onDeleteUsagePeriod(index)
                },
            )
        }
        item {
            EmptyNewUsagePeriod(
                notSelectablePeriods = usageUiState.bookedPeriods + usageUiState.usageDetails.period,
                onAddUsagePeriod = {
                    if (it.first != null && it.second != null) {
                        val periodAdded = usageUiState.usageDetails.period
                        periodAdded.add(UsagePeriod(it.first!!, it.second!!))

                        onUsageValueChange(
                            usageUiState.usageDetails.copy(
                                period = periodAdded
                            )
                        )
                    }
                }
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
            onItemSelectionValueChange = {},
            onItemValueChange = {},
            onUsageValueChange = {},
            onSaveUsageClick = {},
            onSaveItem = {},
        )
    }
}
