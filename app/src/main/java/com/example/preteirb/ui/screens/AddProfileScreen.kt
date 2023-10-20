package com.example.preteirb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.example.preteirb.R

@Composable
fun AddProfileScreen(modifier: Modifier = Modifier) {
    var username by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.username)) },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.add_profile))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddProfileScreenPreview() {
    AppTheme {
        AddProfileScreen()
    }
}