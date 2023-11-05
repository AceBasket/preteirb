package com.example.preteirb.ui.screens.newitemusage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.preteirb.R
import java.time.Instant
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUsagePeriod(
    onNewUsagePeriodSelected: (Pair<Long?, Long?>) -> Unit,
    onAddUsagePeriod: () -> Unit,
    onDeleteUsagePeriod: () -> Unit,
    onUpdateUsagePeriod: (Pair<Long?, Long?>) -> Unit,
    isLastUsagePeriodEntry: Boolean,
    modifier: Modifier = Modifier,
) {
    var startDateTime: Long? by rememberSaveable {
        mutableStateOf(null)
    }
    
    var endDateTime: Long? by rememberSaveable {
        mutableStateOf(null)
    }
    
    var isShowDatePicker by rememberSaveable {
        mutableStateOf(false)
    }
    
    var isSelectEndDate by rememberSaveable {
        mutableStateOf(false)
    }
    
    var isModifiable by rememberSaveable { mutableStateOf(false) }
    
    
    //var startDateTime: Long? by rememberSaveable { mutableStateOf(null) }
    //var endDateTime: Long? by rememberSaveable { mutableStateOf(null) }
    
    Row(
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        OutlinedButton(
            onClick = { isShowDatePicker = true },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)),
            modifier = Modifier.weight(0.5f),
            enabled = isModifiable || isLastUsagePeriodEntry,
        ) {
            Column {
                Text(
                    text = "Start date:",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (startDateTime == 0L || startDateTime == null) ""
                    else DateTimeFormatter.ISO_INSTANT.format(
                        Instant.ofEpochMilli(
                            startDateTime ?: 0L
                        )
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
            modifier = Modifier.weight(0.5f),
            enabled = isModifiable || isLastUsagePeriodEntry,
        ) {
            Column {
                Text(
                    text = "End date:",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (endDateTime == 0L || endDateTime == null) ""
                    else DateTimeFormatter.ISO_INSTANT.format(
                        Instant.ofEpochMilli(
                            endDateTime ?: 0L
                        )
                    )
                )
            }
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
        
        if (isLastUsagePeriodEntry) {
            IconButton(
                onClick = { onAddUsagePeriod() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_usage_period)
                )
            }
        } else {
            if (isModifiable) {
                IconButton(
                    onClick = {
                        onUpdateUsagePeriod(Pair(startDateTime, endDateTime))
                        isModifiable = false
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.confirm_edit_usage_period)
                    )
                }
            } else {
                IconButton(
                    onClick = { isModifiable = true },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit_usage_period)
                    )
                }
            }
            IconButton(
                onClick = { onDeleteUsagePeriod() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                enabled = false
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_usage_period)
                )
            }
        }
        
    }
    
    if (isShowDatePicker) {
        UsagePeriodPickerDialog(
            dateRangePickerState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = startDateTime,
                initialSelectedEndDateMillis = endDateTime,
            ),
            onDismissDialog = { isShowDatePicker = false },
            onConfirmSelection = {
                startDateTime = it.selectedStartDateMillis
                endDateTime = it.selectedEndDateMillis
                isShowDatePicker = false
                onNewUsagePeriodSelected(Pair(startDateTime, endDateTime))
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


@Preview(showBackground = true)
@Composable
fun NewUsagePeriodPreview() {
    AppTheme {
        NewUsagePeriod(
            onAddUsagePeriod = {},
            onNewUsagePeriodSelected = {},
            onDeleteUsagePeriod = {},
            onUpdateUsagePeriod = {},
            isLastUsagePeriodEntry = false,
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