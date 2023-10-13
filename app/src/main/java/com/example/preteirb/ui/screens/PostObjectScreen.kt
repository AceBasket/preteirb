package com.example.preteirb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import java.text.DateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostObjectScreen(modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    var startDate by remember {
        mutableLongStateOf(calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
    }
    
    var endDate by remember {
        mutableLongStateOf(calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
    }
    
    // set the initial dates
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate,
        initialSelectedEndDateMillis = endDate
    )
    
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        startDate = dateRangePickerState.selectedStartDateMillis!!
                        endDate = dateRangePickerState.selectedEndDateMillis!!
                    }) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                    }) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DateRangePicker(
                    state = dateRangePickerState,
                    modifier = Modifier.height(height = 500.dp) // if I don't set this, dialog's buttons are not appearing
                )
            }
        }
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Available from ${DateFormat.getDateInstance().format(startDate)} " +
                        "to ${DateFormat.getDateInstance().format(endDate)}"
            )
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