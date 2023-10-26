package com.example.preteirb.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.Item
import com.example.preteirb.model.AppViewModelProvider
import com.example.preteirb.model.ItemDetails
import com.example.preteirb.model.ItemEntryViewModel
import com.example.preteirb.model.ItemUiState
import com.example.preteirb.model.UsageDetails
import com.example.preteirb.model.UsageEntryViewModel
import com.example.preteirb.model.UsageUiState
import com.example.preteirb.utils.getLocalDateTimeFromEpoch
import kotlinx.coroutines.launch
import java.text.DateFormat

@OptIn(ExperimentalMaterial3Api::class)
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
    onItemValueChange: (ItemDetails) -> Unit = {},
    onUsageValueChange: (UsageDetails) -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    var usagesCount by rememberSaveable {
        mutableIntStateOf(1)
    }
    
    Column(
        modifier = modifier
    ) {
        ObjectSelection(
            objectList = emptyList(),
            itemDetails = itemUiState.itemDetails,
            onValueChange = onItemValueChange,
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
        
        
    }
}

@Composable
fun ObjectSelection(
    objectList: List<Item>,
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    // Up Icon when expanded and down icon when collapsed
    val dropdownIcon = if (isExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    
    Column(
        modifier = modifier
    ) {
        // Create an Outlined Text Field
        // with icon and not expanded
        OutlinedTextField(
            value = selectedText,
            onValueChange = {
                selectedText = it
                onValueChange(
                    itemDetails.copy(
                        // should never return 0 because list created from objectList
                        id = objectList.find { item -> item.name == it }?.itemId ?: 0
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Label") },
            trailingIcon = {
                Icon(dropdownIcon, "contentDescription",
                    Modifier.clickable { isExpanded = !isExpanded })
            }
        )
        
        // Create a drop-down menu with list of objects,
        // when clicked, set the Text Field text as the object selected
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            objectList.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        selectedText = it.name
                        isExpanded = false
                    },
                )
            }
            
            // last item is a button to add an object
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.add_object)) },
                onClick = { /*TODO*/ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUsagePeriod(
    onAddUsagePeriod: (Pair<Long?, Long?>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isShowDatePicker by rememberSaveable {
        mutableStateOf(false)
    }
    
    var isSelectEndDate by rememberSaveable {
        mutableStateOf(false)
    }
    
    var startDateTime: Long? by rememberSaveable { mutableStateOf(null) }
    var endDateTime: Long? by rememberSaveable { mutableStateOf(null) }
    
    Row(
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        OutlinedButton(
            onClick = { isShowDatePicker = true },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)),
            modifier = Modifier.weight(0.5f),
        ) {
            Column {
                Text(
                    text = "Start date:",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (startDateTime == 0.toLong() || startDateTime == null) ""
                    else DateFormat.getDateInstance().format(
                        getLocalDateTimeFromEpoch(startDateTime!!)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
        OutlinedButton(
            onClick = {
                isShowDatePicker = true
                isSelectEndDate = true
            },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)),
            modifier = Modifier.weight(0.5f)
        ) {
            Column {
                Text(
                    text = "End date:",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (endDateTime == 0.toLong() || endDateTime == null) ""
                    else DateFormat.getDateInstance().format(
                        getLocalDateTimeFromEpoch(endDateTime!!)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
        IconButton(
            onClick = { onAddUsagePeriod(Pair(startDateTime, endDateTime)) },
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_usage_period)
            )
        }
        
    }
    
    if (isShowDatePicker) {
        UsagePeriodPickerDialog(
            dateRangePickerState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = null,
                initialSelectedEndDateMillis = null,
            ),
            onDismissDialog = { isShowDatePicker = false },
            onConfirmSelection = {
                startDateTime = it.selectedStartDateMillis
                endDateTime = it.selectedEndDateMillis
            },
            isSelectEndDate = isSelectEndDate,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsagePeriodPickerDialog(
    dateRangePickerState: DateRangePickerState,
    onDismissDialog: () -> Unit,
    onConfirmSelection: (dateRangePickerState: DateRangePickerState) -> Unit,
    modifier: Modifier = Modifier,
    isSelectEndDate: Boolean = false,
) {
    DatePickerDialog(
        onDismissRequest = onDismissDialog,
        confirmButton = {
            TextButton(onClick = { onConfirmSelection(dateRangePickerState) }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier
    ) {
        if (isSelectEndDate) {
            dateRangePickerState.setSelection(
                startDateMillis = dateRangePickerState.selectedStartDateMillis,
                endDateMillis = null
            )
        }
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier.height(height = 500.dp) // if I don't set this, dialog's buttons are not appearing
        )
    }
    
}

@Composable
fun NewObjectDialog(
    modifier: Modifier = Modifier,
    onSaveObject: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
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
                    //.padding(dimensionResource(id = R.dimen.padding_small)),
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
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.title)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
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
fun NewUsageFormPreview() {
    AppTheme {
        NewUsageForm(
            usageUiState = UsageUiState(),
            itemUiState = ItemUiState(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ObjectSelectionPreview() {
    AppTheme {
        val fakeObjectList = listOf(
            Item(1, "Item 1", "Description 1", 1),
            Item(2, "Item 2", "Description 2", 1),
            Item(3, "Item 3", "Description 3", 1),
            Item(4, "Item 4", "Description 4", 1),
            Item(5, "Item 5", "Description 5", 1),
            Item(6, "Item 6", "Description 6", 1),
        )
        ObjectSelection(
            objectList = fakeObjectList,
            itemDetails = ItemDetails(),
            onValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewUsagePeriodPreview() {
    AppTheme {
        NewUsagePeriod(
            onAddUsagePeriod = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun UsagePeriodPickerDialogPreview() {
    AppTheme {
        UsagePeriodPickerDialog(
            dateRangePickerState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = null,
                initialSelectedEndDateMillis = null,
            ),
            onDismissDialog = {},
            onConfirmSelection = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewObjectDialogPreview() {
    AppTheme {
        NewObjectDialog()
    }
}
