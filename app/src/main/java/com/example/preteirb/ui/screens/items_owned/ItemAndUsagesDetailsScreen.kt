package com.example.preteirb.ui.screens.items_owned

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemAndUsages
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.model.items_owned.ItemAndUsagesDetailsViewModel
import com.example.preteirb.model.items_owned.toItemDetails
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.screens.search.ObjectCard
import epicarchitect.calendar.compose.basis.config.rememberBasisEpicCalendarConfig
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.*

object ItemAndUsagesDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.booked_periods
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun ItemAndUsagesDetailsScreen(
    navigateToBookItem: (itemId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemAndUsagesDetailsViewModel = hiltViewModel(),
) {
    ItemAndUsagesDetails(
        itemAndUsages = viewModel.itemAndUsages
            .collectAsState(
                initial = ItemAndUsages(
                    Item(0, "", "", 0),
                    emptyList()
                )
            ).value,
        onClickOnBookItem = navigateToBookItem,
        modifier = modifier
    )
}

@Composable
fun ItemAndUsagesDetails(
    itemAndUsages: ItemAndUsages,
    onClickOnBookItem: (itemId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ObjectCard(
            item = itemAndUsages.item.toItemDetails(),
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.elevatedCardColors(),
            elevation = CardDefaults.elevatedCardElevation(),
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_large))
        )
        HorizontalDivider()
        ItemBookedPeriods(
            usages = itemAndUsages.usages,
        )
        HorizontalDivider()
        OutlinedButton(
            onClick = { onClickOnBookItem(itemAndUsages.item.itemId) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Book your object")
        }
    }
}

private fun epochMilliToLocalDate(epochMilli: Long): LocalDate {
    return kotlinx.datetime.Instant.fromEpochMilliseconds(epochMilli).toLocalDateTime(
        TimeZone.UTC
    ).date
}

@Composable
fun ItemBookedPeriods(
    usages: List<Usage>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val rangeUsages =
            usages.map { epochMilliToLocalDate(it.startDateTime)..epochMilliToLocalDate(it.endDateTime) }
        val rangeColor = MaterialTheme.colorScheme.primaryContainer
        val epicCalendarPagerState = rememberEpicCalendarPagerState(
            config = rememberEpicCalendarPagerConfig(
                basisConfig = rememberBasisEpicCalendarConfig(
                    rowsSpacerHeight = 4.dp,
                    dayOfWeekViewHeight = 40.dp,
                    dayOfMonthViewHeight = 40.dp,
                    columnWidth = 40.dp,
                    dayOfWeekViewShape = RoundedCornerShape(16.dp),
                    dayOfMonthViewShape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(0.dp),
                    contentColor = Color.Unspecified,
                    displayDaysOfAdjacentMonths = true,
                    displayDaysOfWeek = true
                )
            )
        )
        val monthDisplayed = epicCalendarPagerState.currentMonth.month.getDisplayName(
            TextStyle.FULL, Locale.getDefault()
        ) + " " + epicCalendarPagerState.currentMonth.year.toString()
        Text(
            text = monthDisplayed,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        EpicCalendarPager(
            state = epicCalendarPagerState,
            pageModifier = { _ ->
                Modifier.drawEpicRanges(
                    ranges = rangeUsages,
                    color = rangeColor
                )
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenDetails() {
    AppTheme {
        val fakeData: ItemAndUsages = ItemAndUsages(
            item = Item(
                itemId = 1,
                name = "Item name",
                description = "Item description",
                userOwnerId = 1
            ),
            usages = listOf(
                Usage(
                    usageId = 1,
                    userId = 1,
                    itemId = 1,
                    startDateTime = 1699900000000,
                    endDateTime = 1700680000000
                )
            )
        )
        ItemAndUsagesDetails(itemAndUsages = fakeData, onClickOnBookItem = {})
    }
}