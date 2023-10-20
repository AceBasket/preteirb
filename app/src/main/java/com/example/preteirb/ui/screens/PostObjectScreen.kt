package com.example.preteirb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import java.time.LocalDate
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostObjectScreen(modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    
    
    // set the initial dates
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = null,
        initialSelectedEndDateMillis = null,
    )
    
    var startDate by rememberSaveable { mutableStateOf("") }
    var endDate by rememberSaveable { mutableStateOf("") }
    
    var isShowDatePicker by rememberSaveable {
        mutableStateOf(false)
    }
    
    var isSelectEndDate by rememberSaveable {
        mutableStateOf(false)
    }
    
    Column(
        modifier = modifier
    ) {
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
        Row(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
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
                    Text(text = startDate)
                }
            }
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
            OutlinedButton(
                onClick = { isShowDatePicker = true },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)),
                modifier = Modifier.weight(0.5f)
            ) {
                Column {
                    Text(
                        text = "End date:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = endDate)
                }
            }
            
        }
        
        if (isShowDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    isShowDatePicker = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        isShowDatePicker = false
                        startDate = dateRangePickerState.selectedStartDateMillis?.let {
                            LocalDate.ofEpochDay(it / 86400000)
                                .toString()
                        } ?: ""
                        endDate = dateRangePickerState.selectedEndDateMillis?.let {
                            LocalDate.ofEpochDay(it / 86400000)
                                .toString()
                        } ?: ""
                    }) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isShowDatePicker = false
                    }) {
                        Text(text = "Cancel")
                    }
                }
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
        
    }
}

@Preview(showBackground = true)
@Composable
fun PostObjectScreenPreview() {
    AppTheme {
        PostObjectScreen()
    }
}