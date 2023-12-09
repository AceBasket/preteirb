package com.example.preteirb.ui.screens.items_booked

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.preteirb.R
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemWithOwner
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsageWithItemAndUser
import com.example.preteirb.data.usage.getShortenedDateFormat
import com.example.preteirb.data.user.User
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
                item = data.item,
                usage = data.usage,
                modifier = modifier
                    .padding(
//                        start = dimensionResource(id = R.dimen.padding_large),
//                        end = dimensionResource(id = R.dimen.padding_large),
                        bottom = dimensionResource(id = R.dimen.padding_small)
                    )
            )
        }
    }

}

@Composable
fun ItemBookedCard(
    item: ItemWithOwner,
    usage: Usage,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
//                    .aspectRatio(1f/1f)
                    .size(dimensionResource(id = R.dimen.image_size_medium))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.item.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Belongs to ${item.owner.username}",
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
                    item = ItemWithOwner(
                        item = Item(0, "Tondeuse", "", 0),
                        owner = User(0, "Sarah")
                    ),
                    usage = Usage(0, 0, 0, 1701613238000, 1702623238000)
                ),
                UsageWithItemAndUser(
                    item = ItemWithOwner(
                        item = Item(0, "Marteau", "", 0),
                        owner = User(0, "Jean")
                    ),
                    usage = Usage(0, 0, 0, 1701613238000, 1702623238000)
                ),
                UsageWithItemAndUser(
                    item = ItemWithOwner(
                        item = Item(0, "Sécateur", "", 0),
                        owner = User(0, "Camille")
                    ),
                    usage = Usage(0, 0, 0, 1701613238000, 1702623238000)
                ),
                UsageWithItemAndUser(
                    item = ItemWithOwner(
                        item = Item(0, "Pelle", "", 0),
                        owner = User(0, "Toto")
                    ),
                    usage = Usage(0, 0, 0, 1701613238000, 1702623238000)
                ),
            )
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun ItemsBookedCardPreview() {
    ItemBookedCard(
        item = ItemWithOwner(
            item = Item(0, "Tondeuse", "", 0),
            owner = User(0, "Jean")
        ),
        usage = Usage(0, 0, 0, 1701613238000, 1702623238000)
    )
}