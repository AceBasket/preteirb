package com.example.preteirb.ui.screens.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.preteirb.R
import com.example.preteirb.model.ListItemsViewModel
import com.example.preteirb.model.toItemDetails
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.screens.search.ObjectList

object ListItemsDestination : NavigationDestination {
    override val route = "list_items"
    override val titleRes = R.string.your_objects
}

@Composable
fun ListItemsScreen(
    navigateToItemDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListItemsViewModel = hiltViewModel()
) {
    ObjectList(
        objects = viewModel.listItemsUiState.collectAsState().value.itemsOwned.map { it.toItemDetails() },
        onItemClick = { navigateToItemDetails(it) },
        modifier = modifier
    )
}