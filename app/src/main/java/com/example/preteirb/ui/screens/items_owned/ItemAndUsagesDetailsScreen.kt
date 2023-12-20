package com.example.preteirb.ui.screens.items_owned

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.placeholder
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.common.CustomGlideImage
import com.example.preteirb.common.ItemEditor
import com.example.preteirb.data.item.ItemAndUsages
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsageWithStringDate
import com.example.preteirb.data.usage.toUsage
import com.example.preteirb.model.items_owned.ItemAndUsagesDetailsViewModel
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.items_owned.ItemUiState
import com.example.preteirb.ui.navigation.NavigationDestination
import epicarchitect.calendar.compose.basis.config.rememberBasisEpicCalendarConfig
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

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
    val coroutineScope = rememberCoroutineScope()
    ItemAndUsagesDetails(
        itemAndUsages = viewModel.itemAndUsages
            .collectAsState(
                initial = ItemAndUsages(
                    0,
                    "",
                    "",
                    null,
                    0,
                    emptyList()
                )
            ).value,
        onClickOnBookItem = navigateToBookItem,
        itemUiState = viewModel.itemUiState,
        updateItemUiState = viewModel::updateUiState,
        saveItemModifications = {
            coroutineScope.launch {
                viewModel.saveItem()
            }
        },
        modifier = modifier
    )
}

@Composable
fun ItemAndUsagesDetails(
    itemAndUsages: ItemAndUsages,
    onClickOnBookItem: (itemId: Int) -> Unit,
    itemUiState: ItemUiState,
    updateItemUiState: (ItemDetails) -> Unit,
    saveItemModifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isShowEditDialog by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        DetailsHeadline(
            item = itemUiState.itemDetails,
            onClick = { isShowEditDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))
        ItemBookedPeriods(
            usages = itemAndUsages.usages.map { it.toUsage() },
        )
        HorizontalDivider()
        OutlinedButton(
            onClick = { onClickOnBookItem(itemAndUsages.id) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.book_item))
        }
    }

    if (isShowEditDialog) {
        ItemEditor(
            itemUiState = itemUiState,
            updateUiState = updateItemUiState,
            onConfirmation = {
                saveItemModifications()
                isShowEditDialog = false
            },
            onDismissRequest = { isShowEditDialog = false },
        )
    }
}

private fun epochMilliToLocalDate(epochMilli: Long): LocalDate {
    return kotlinx.datetime.Instant.fromEpochMilliseconds(epochMilli).toLocalDateTime(
        TimeZone.UTC
    ).date
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsHeadline(
    item: ItemDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .border(
                1.dp,
                Color.Black,
                RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_medium))
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineLarge,
                fontStyle = FontStyle.Italic,
            )
            CustomGlideImage(
                model = item.image,
                placeholder = ImageVector.vectorResource(R.drawable.baseline_image_24),
                contentDescription = item.name,
                loading = placeholder(R.drawable.loading_img),
                failure = placeholder(R.drawable.ic_broken_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.image_size_large))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)))
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.baseline_edit_24),
            contentDescription = stringResource(id = R.string.edit_object),
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
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
                    rowsSpacerHeight = dimensionResource(id = R.dimen.padding_extra_small),
                    dayOfWeekViewHeight = 40.dp,
                    dayOfMonthViewHeight = 40.dp,
                    columnWidth = 40.dp,
                    dayOfWeekViewShape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_medium)),
                    dayOfMonthViewShape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_medium)),
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
            style = MaterialTheme.typography.headlineSmall,
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
        val fakeData = ItemAndUsages(
            id = 1,
            name = "Item name",
            description = "Item description",
            image = null,
            ownerId = 1,
            usages = listOf(
                UsageWithStringDate(
                    id = 1,
                    userUsingItemId = 1,
                    itemUsedId = 1,
                    startDate = "2023-12-04",
                    endDate = "2023-12-08"
                )
            )
        )
        ItemAndUsagesDetails(
            itemAndUsages = fakeData,
            onClickOnBookItem = {},
            itemUiState = ItemUiState(),
            updateItemUiState = {},
            saveItemModifications = {},
        )
//        DetailsHeadline(itemAndUsages = fakeData, onClick = {})
    }
}