package com.example.preteirb.ui.screens.items_owned

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.common.ItemCreator
import com.example.preteirb.data.cache.items_owned.ItemOwned
import com.example.preteirb.data.cache.items_owned.toItemDetails
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.items_owned.ItemEntryViewModel
import com.example.preteirb.model.items_owned.ItemUiState
import com.example.preteirb.model.items_owned.ListItemsViewModel
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import com.example.preteirb.ui.navigation.NavigationDestination
import com.example.preteirb.ui.screens.search.ObjectList
import kotlinx.coroutines.launch

object ListItemsDestination : NavigationDestination {
    override val route = "list_items"
    override val titleRes = R.string.your_objects
}

@Composable
fun ListItemsScreen(
    navigateToItemDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    listItemsViewModel: ListItemsViewModel = hiltViewModel(),
    newItemViewModel: ItemEntryViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val objectList by listItemsViewModel.listItemsUiState.collectAsState()

    ListItemsScreenContent(
        objectList = objectList,
        onItemClick = { navigateToItemDetails(it) },
        newItemUiState = newItemViewModel.itemUiState,
        onNewItemValueChange = newItemViewModel::updateUiState,
        onSaveNewItem = {
            coroutineScope.launch {
                newItemViewModel.saveItem()
                newItemViewModel.whipeItemUiState()
            }
        },
        whipeNewItemUiState = newItemViewModel::whipeItemUiState,
        modifier = modifier
    )
}

@Composable
fun ListItemsScreenContent(
    objectList: ItemsOwnedUiState,
    onItemClick: (Int) -> Unit,
    newItemUiState: ItemUiState,
    onNewItemValueChange: (ItemDetails) -> Unit,
    onSaveNewItem: () -> Unit,
    whipeNewItemUiState: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowObjectDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isShowObjectDialog = true },
                text = {
                    Text(
                        text = stringResource(id = R.string.add_object),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                    )
                },
                modifier = Modifier.testTag("addObjectFAB")
            )
        },
//        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        ObjectList(
            objects = objectList.itemsOwned.map { it.toItemDetails() },
            onItemClick = onItemClick,
            modifier = modifier.padding(paddingValues)
        )
    }

    if (isShowObjectDialog) {
        ItemCreator(
            itemUiState = newItemUiState,
            updateUiState = onNewItemValueChange,
            onConfirmation = {
                onSaveNewItem()
                isShowObjectDialog = false
            },
            onDismissRequest = {
                isShowObjectDialog = false
                whipeNewItemUiState()
            },
            modifier = Modifier.testTag("addObjectDialog")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemsScreenContentPreview() {
    AppTheme {
        val fakeObjectList = listOf(
            ItemOwned(
                id = 1,
                name = "Item 1",
                description = "Description 1",
                imageUrl = null,
                ownerId = 1,
            ),
            ItemOwned(
                id = 2,
                name = "Item 2",
                description = "Description 2",
                imageUrl = null,
                ownerId = 1,
            ),
            ItemOwned(
                id = 3,
                name = "Item 3",
                description = "Description 3",
                imageUrl = null,
                ownerId = 1,
            ),
        )
        ListItemsScreenContent(
            objectList = ItemsOwnedUiState(itemsOwned = fakeObjectList),
            onItemClick = {},
            newItemUiState = ItemUiState(),
            onNewItemValueChange = {},
            onSaveNewItem = {},
            whipeNewItemUiState = {},
        )
    }
}