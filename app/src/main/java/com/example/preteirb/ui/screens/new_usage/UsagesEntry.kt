package com.example.preteirb.ui.screens.new_usage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.new_usage.UsageDetails
import com.example.preteirb.model.new_usage.UsagePeriod
import com.example.preteirb.model.new_usage.UsageUiState

@Composable
fun AddUsages(
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
                modifier = Modifier.testTag("usagePeriodListItem"),
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
                },
                modifier = Modifier.testTag("emptyNewUsagePeriod"),
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
            modifier = Modifier.testTag("saveUsagePeriodsButton")
        ) {
            Text(text = stringResource(id = R.string.post))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NewUsageFormPreview() {
    AppTheme {
        AddUsages(
            usageUiState = UsageUiState(),
            onUsageValueChange = {},
            onSaveUsageClick = {},
            validateLastUsagePeriod = { true }
        )
    }
}
