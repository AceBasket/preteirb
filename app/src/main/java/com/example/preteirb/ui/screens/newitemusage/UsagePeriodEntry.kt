package com.example.preteirb.ui.screens.newitemusage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.preteirb.utils.getLocalDateTimeFromEpoch
import java.text.DateFormat

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