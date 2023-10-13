package com.example.preteirb.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.example.preteirb.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        IconAndLabelCard(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_broken_image),
                    contentDescription = null
                )
            },
            label = "Username"
        )
        IconAndLabelCard(
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings icon"
                )
            },
            label = "Settings"
        )
        IconAndLabelCard(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_switch_account_24),
                    contentDescription = "Switch account icon"
                )
            },
            label = "Switch account"
        )
        IconAndLabelCard(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = "Log out icon"
                )
            },
            label = "Log out"
        )
        
    }
}

@Composable
fun IconAndLabelCard(
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        icon()
        Text(
            text = label
        )
    }
    
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen()
    }
}