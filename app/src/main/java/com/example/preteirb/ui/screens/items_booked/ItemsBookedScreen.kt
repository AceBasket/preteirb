package com.example.preteirb.ui.screens.items_booked

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.placeholder
import com.example.preteirb.R
import com.example.preteirb.common.CustomGlideImage
import com.example.preteirb.data.item.ItemWithOwner
import com.example.preteirb.data.usage.UsageWithItemAndUser
import com.example.preteirb.data.usage.getShortenedDateFormat
import com.example.preteirb.data.user.UserDto
import com.example.preteirb.model.items_booked.ItemsBookedUiState
import com.example.preteirb.model.items_booked.ItemsBookedViewModel
import com.example.preteirb.ui.navigation.NavigationDestination

object ItemsBookedDestination : NavigationDestination {
    override val route = "items_booked"
    override val titleRes = R.string.bookings
}

@Composable
fun ItemsBookedScreen(
    modifier: Modifier = Modifier,
    viewModel: ItemsBookedViewModel = hiltViewModel(),
) {
    val uiState = viewModel.itemsBookedAndNotOwnedByUser.collectAsState().value
    ItemsBooked(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
fun ItemsBooked(
    uiState: ItemsBookedUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        items(uiState.bookings) { data ->
            ItemBookedCard(
                usage = data,
                modifier = modifier
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_small)
                    )
            )
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemBookedCard(
    usage: UsageWithItemAndUser,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier,
    ) {
        Row {
            CustomGlideImage(
                model = usage.item.image,
                placeholder = ImageVector.vectorResource(R.drawable.baseline_image_24),
                contentDescription = usage.item.name,
                loading = placeholder(R.drawable.loading_img),
                failure = placeholder(R.drawable.baseline_broken_image_24),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.image_size_medium))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = usage.item.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Belongs to ${usage.item.owner.username}",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = getShortenedDateFormat(usage.startDate),
                    )
                    Text(
                        text = " - ",
                    )
                    Text(
                        text = getShortenedDateFormat(usage.endDate),
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ItemsBookedPreview() {
    ItemsBooked(
        uiState = ItemsBookedUiState(
            bookings = listOf(
                UsageWithItemAndUser(
                    id = 0,
                    startDate = 1701613238000,
                    endDate = 1702623238000,
                    item = ItemWithOwner(
                        id = 0,
                        name = "Tondeuse",
                        description = "",
                        image = null,
                        owner = UserDto(0, "Jean", ""),
                    ),
                    user = 0
                ),
                UsageWithItemAndUser(
                    id = 1,
                    startDate = 1701613238000,
                    endDate = 1702623238000,
                    item = ItemWithOwner(
                        id = 1,
                        name = "Perceuse",
                        description = "",
                        image = null,
                        owner = UserDto(1, "Céline", "")
                    ),
                    user = 0
                ),
                UsageWithItemAndUser(
                    id = 2,
                    startDate = 1701613238000,
                    endDate = 1702623238000,
                    item = ItemWithOwner(
                        id = 2,
                        name = "Marteau",
                        description = "",
                        image = null,
                        owner = UserDto(2, "Pierre", "")
                    ),
                    user = 0
                ),
                UsageWithItemAndUser(
                    id = 3,
                    startDate = 1701613238000,
                    endDate = 1702623238000,
                    item = ItemWithOwner(
                        id = 3,
                        name = "Tournevis",
                        description = "",
                        image = null,
                        owner = UserDto(3, "Paul", "")
                    ),
                    user = 0
                ),
            )
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun ItemsBookedCardPreview() {
    ItemBookedCard(
        usage = UsageWithItemAndUser(
            id = 0,
            startDate = 1701613238000,
            endDate = 1702623238000,
            item = ItemWithOwner(
                id = 0,
                name = "Tondeuse",
                description = "",
                image = null,
                owner = UserDto(0, "Jean", "")
            ),
            user = 0
        ),
    )
}