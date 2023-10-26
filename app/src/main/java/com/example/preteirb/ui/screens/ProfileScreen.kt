package com.example.preteirb.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.example.preteirb.R

data class ProfileScreenItems(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
)

@Composable
fun ProfileScreen(
    menuItems: List<ProfileScreenItems>,
    modifier: Modifier = Modifier,
    onClickOnMenuItem: (ProfileScreenItems) -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        menuItems.forEach() { menuItem ->
            Box(modifier = Modifier.clickable { onClickOnMenuItem(menuItem) }) {
                IconAndLabelCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = menuItem.icon),
                            contentDescription = null //TODO
                        )
                    },
                    label = stringResource(id = menuItem.label),
                    modifier = Modifier
                        .padding(bottom = dimensionResource(id = R.dimen.padding_small))
                )
            }
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
        val fakeProfileScreenItems = listOf(
            ProfileScreenItems(
                label = R.string.username,
                icon = R.drawable.ic_broken_image
            ),
            ProfileScreenItems(
                label = R.string.settings,
                icon = R.drawable.baseline_settings_24
            ),
            ProfileScreenItems(
                label = R.string.switch_account,
                icon = R.drawable.baseline_switch_account_24
            ),
            ProfileScreenItems(
                label = R.string.logout,
                icon = R.drawable.baseline_logout_24
            ),
        )
        ProfileScreen(
            menuItems = fakeProfileScreenItems,
            onClickOnMenuItem = { }
        )
    }
}