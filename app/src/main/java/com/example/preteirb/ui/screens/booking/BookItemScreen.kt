package com.example.preteirb.ui.screens.booking

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.preteirb.R
import com.example.preteirb.model.AppViewModelProvider
import com.example.preteirb.model.BookItemsViewModel
import com.example.preteirb.model.ItemDetails
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.screens.newitemusage.AddUsages
import com.example.preteirb.ui.screens.search.ObjectCard
import kotlinx.coroutines.launch

object BookItemDestination : NavigationDestination {
    override val route = "book_item"
    override val titleRes = R.string.book_item
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun BookItemScreen(
    modifier: Modifier = Modifier,
    navigateToHomeScreen: () -> Unit,
    bookItemsViewModel: BookItemsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val item: ItemDetails = bookItemsViewModel.itemToBookDetails.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier) {
        ObjectCard(
            item = item,
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.elevatedCardColors(),
            elevation = CardDefaults.elevatedCardElevation(),
        )
        AddUsages(
            usageUiState = bookItemsViewModel.uiState,
            usagePeriodsCount = bookItemsViewModel.usagePeriodsCount,
            onAddUsagePeriod = bookItemsViewModel::addUsagePeriod,
            onDeleteUsagePeriod = bookItemsViewModel::deleteUsagePeriod,
            onUsageValueChange = bookItemsViewModel::updateUiState,
            onSaveUsageClick = {
                coroutineScope.launch {
                    bookItemsViewModel.saveUsage()
                }
                navigateToHomeScreen()
            }
        
        )
    }
}

/* @Composable
fun ItemDetailsCard(
    item: ItemDetails,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            //modifier = Modifier
            //.padding(dimensionResource(id = R.dimen.spacing_medium))
            //.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = "Item Image"
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_extra_small)))
            Column {
                Text(text = item.name)
                Text(text = item.description)
            }
        }
    }
} */
