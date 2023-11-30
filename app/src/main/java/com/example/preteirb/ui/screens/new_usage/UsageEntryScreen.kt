package com.example.preteirb.ui.screens.new_usage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.*
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.items_owned.ItemEntryViewModel
import com.example.preteirb.model.items_owned.ItemUiState
import com.example.preteirb.model.items_owned.ItemsOwnedUsageEntryViewModel
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import com.example.preteirb.model.new_usage.UsageDetails
import com.example.preteirb.model.new_usage.UsagePeriod
import com.example.preteirb.model.new_usage.UsageUiState
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.screens.items_owned.NewObjectDialog
import kotlinx.coroutines.launch

object ItemOwnedUsageEntryDestination : NavigationDestination {
    override val route = "item_owned_usage_entry"
    override val titleRes = R.string.items_owned_new_usages
}

@Composable
fun NewUsageScreen(
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    itemViewModel: ItemEntryViewModel = hiltViewModel(),
    itemsOwnedUsagesViewModel: ItemsOwnedUsageEntryViewModel = hiltViewModel(),
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
                itemsOwnedUsagesViewModel.saveUsage(it)
            }
            navigateToHomeScreen()
        },
        onSaveItem = {
            coroutineScope.launch {
                itemViewModel.saveItem()
            }
        },
        validateLastUsagePeriod = itemsOwnedUsagesViewModel::validateLastPeriod,
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
    onSaveUsageClick: (UsagePeriod?) -> Unit,
    onSaveItem: () -> Unit,
    validateLastUsagePeriod: (UsagePeriod) -> Boolean,
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
            validateLastUsagePeriod = validateLastUsagePeriod,
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
    onSaveUsageClick: (UsagePeriod?) -> Unit,
    validateLastUsagePeriod: (UsagePeriod) -> Boolean,
    modifier: Modifier = Modifier
) {
    var lastUsagePeriod: UsagePeriod? by remember { mutableStateOf(null) } // will be updated with the values from the last usage period

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
                },
            )
        }
        item {
            EmptyNewUsagePeriod(
                notSelectablePeriods = usageUiState.bookedPeriods + usageUiState.usageDetails.period,
                onNewUsagePeriodSelected = {
                    if (it.first != null && it.second != null) {
                        lastUsagePeriod = UsagePeriod(it.first!!, it.second!!)
                        validateLastUsagePeriod(lastUsagePeriod!!)
                    }
                },
                onAddUsagePeriod = {
                    if (it.first != null && it.second != null) {
                        val periodAdded = usageUiState.usageDetails.period
                        periodAdded.add(UsagePeriod(it.first!!, it.second!!))

                        onUsageValueChange(
                            usageUiState.usageDetails.copy(
                                period = periodAdded
                            )
                        )

                        lastUsagePeriod = null // reset last usage period
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
            onClick = { onSaveUsageClick(lastUsagePeriod) },
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
            validateLastUsagePeriod = { true },
        )
    }
}
