package com.example.preteirb.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.preteirb.R

@Composable
fun ProfileScreen(
    navigateToSelectProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop, // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape) // clip to the circle shape
                    .border(2.dp, Color.Gray, CircleShape) // add a border (optional)
            )
            Text(
                text = "Username",
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
        var isExpanded by remember { mutableStateOf(false) }
        SettingsSection(isExpanded = isExpanded, onHeaderClick = { isExpanded = !isExpanded })
        Row {
            Icon(
                painter = painterResource(id = R.drawable.baseline_logout_24),
                contentDescription = "Logout icon"
            )
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
            Text(
                text = stringResource(id = R.string.logout)
            )
        }
    }
}

@Composable
fun IconAndLabelCard(
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        icon()
        Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
        Text(
            text = label
        )
    }
    
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            navigateToSelectProfile = { }
        )
    }
}


// -------------------------------- Settings expandable section -------------------------------

@Composable
fun SectionItem() {
    Text(text = "No settings available yet")
}

@Composable
fun SettingsSectionHeader(
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onHeaderClicked() }
            .background(Color.LightGray)
        //.padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Icon(Icons.Default.Settings, contentDescription = "Settings icon")
        Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
        Text(
            text = stringResource(id = R.string.settings),
            modifier = Modifier.weight(1.0f)
        )
        if (isExpanded) {
            ExpandedCheveronIcon()
        } else {
            CollapsedCheveronIcon()
        }
    }
}

@Composable
fun ExpandedCheveronIcon() {
    Icon(
        Icons.Default.KeyboardArrowUp,
        contentDescription = "Expanded chevron icon",
    )
}

@Composable
fun CollapsedCheveronIcon() {
    Icon(
        Icons.Default.KeyboardArrowDown,
        contentDescription = "collapsed chevron icon",
    )
}

@Composable
fun SettingsSection(
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SettingsSectionHeader(
            isExpanded = isExpanded,
            onHeaderClicked = onHeaderClick
        )
        if (isExpanded) {
            SectionItem()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandedSectionPreview() {
    AppTheme {
        SettingsSection(
            isExpanded = true,
            onHeaderClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CollapsedSectionPreview() {
    AppTheme {
        SettingsSection(
            isExpanded = false,
            onHeaderClick = { }
        )
    }
}