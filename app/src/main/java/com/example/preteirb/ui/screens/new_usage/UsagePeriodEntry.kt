package com.example.preteirb.ui.screens.new_usage

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.usage.getShortenedDateFormat
import com.example.preteirb.model.new_usage.UsagePeriod

@Composable
fun EmptyNewUsagePeriod(
    notSelectablePeriods: List<UsagePeriod>,
    onNewUsagePeriodSelected: (Pair<Long?, Long?>) -> Unit,
    onAddUsagePeriod: (Pair<Long?, Long?>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        var startPeriod: Long? by rememberSaveable { mutableStateOf(null) }
        var endPeriod: Long? by rememberSaveable { mutableStateOf(null) }
        NewUsagePeriod(
            onNewUsagePeriodSelected = {
                startPeriod = it.first
                endPeriod = it.second
                onNewUsagePeriodSelected(it)
            },
            notSelectablePeriods = notSelectablePeriods,
            isModifiable = true,
            trailingButtons = {
                IconButton(
                    onClick = {
                        onAddUsagePeriod(Pair(startPeriod, endPeriod))
                        it()
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_usage_period)
                    )
                }

            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun EmptyNewUsagePeriodPreview() {
    AppTheme {
        EmptyNewUsagePeriod(
            notSelectablePeriods = listOf(),
            onAddUsagePeriod = {},
            onNewUsagePeriodSelected = {},
        )
    }
}

@Composable
fun UsagePeriodListItem(
    onNewUsagePeriodSelected: (Pair<Long?, Long?>) -> Unit,
    notSelectablePeriods: List<UsagePeriod>,
    usagePeriod: UsagePeriod,
    onDeleteUsagePeriod: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        var isModifiable by rememberSaveable { mutableStateOf(false) }
        NewUsagePeriod(
            onNewUsagePeriodSelected = onNewUsagePeriodSelected,
            notSelectablePeriods = notSelectablePeriods,
            isModifiable = isModifiable,
            initialValues = Pair(usagePeriod.start, usagePeriod.end),
            trailingButtons = {
                if (isModifiable) {
                    ValidateEditionUsagePeriodButton(
                        onClick = { isModifiable = false },
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                } else {
                    EditUsagePeriodButton(
                        onClick = { isModifiable = true },
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small))
                    )
                }

                DeleteUsagePeriodButton(
                    onClick = onDeleteUsagePeriod,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small))
                )

            }
        )


    }
}

@Preview(showBackground = true)
@Composable
fun UsagePeriodListItemPreview() {
    AppTheme {
        UsagePeriodListItem(
            onNewUsagePeriodSelected = {},
            notSelectablePeriods = listOf(),
            usagePeriod = UsagePeriod(1701427036000, 0L),
            onDeleteUsagePeriod = {},
        )
    }
}

@Composable
fun ValidateEditionUsagePeriodButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(id = R.string.confirm_edit_usage_period)
        )
    }
}

@Composable
fun EditUsagePeriodButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(id = R.string.edit_usage_period)
        )
    }
}

@Composable
fun DeleteUsagePeriodButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete_usage_period)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUsagePeriod(
    onNewUsagePeriodSelected: (Pair<Long?, Long?>) -> Unit,
    notSelectablePeriods: List<UsagePeriod>,
    isModifiable: Boolean,
    trailingButtons: @Composable RowScope.(() -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    initialValues: Pair<Long?, Long?> = Pair(null, null),
) {
    var startDateTime: Long? by rememberSaveable {
        mutableStateOf(initialValues.first)
    }

    var endDateTime: Long? by rememberSaveable {
        mutableStateOf(initialValues.second)
    }

    var isShowDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    var isSelectEndDate by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        UsageDateField(
            onClick = { isShowDatePicker = true },
            isModifiable = isModifiable,
            dateTime = startDateTime,
            label = R.string.from,
            modifier = Modifier.testTag("startUsageDateField")
        )
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
        UsageDateField(
            onClick = {
                isShowDatePicker = true
                isSelectEndDate = true
            },
            isModifiable = isModifiable,
            dateTime = endDateTime,
            label = R.string.to,
            modifier = Modifier.testTag("endUsageDateField")
        )
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
        trailingButtons {
            if (initialValues.first == null && initialValues.second == null) {
                startDateTime = null
                endDateTime = null
            }
        }
    }

    if (isShowDatePicker) {
        UsagePeriodPickerDialog(
            dateRangePickerState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = startDateTime,
                initialSelectedEndDateMillis = endDateTime,
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        // is selectable if it's not in any of the notSelectablePeriods
                        return utcTimeMillis > System.currentTimeMillis()
                                && notSelectablePeriods.none { usagePeriod ->
                            utcTimeMillis in usagePeriod.start..usagePeriod.end
                        }
                    }
                }
            ),
            onDismissDialog = { isShowDatePicker = false },
            onConfirmSelection = {
                startDateTime = it.selectedStartDateMillis
                endDateTime = it.selectedEndDateMillis
                isShowDatePicker = false
                onNewUsagePeriodSelected(Pair(startDateTime, endDateTime))
            },
            isSelectEndDate = isSelectEndDate,
            modifier = Modifier.testTag("usagePeriodPickerDialog")
        )
    }
}

@Composable
fun RowScope.UsageDateField(
    onClick: () -> Unit,
    isModifiable: Boolean,
    dateTime: Long?,
    @StringRes label: Int,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)),
        modifier = modifier.weight(0.5f),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_small)),
        enabled = isModifiable
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = label),
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = if (dateTime == 0L || dateTime == null) ""
                else getShortenedDateFormat(dateTime),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
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