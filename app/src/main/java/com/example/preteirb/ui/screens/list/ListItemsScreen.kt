package com.example.preteirb.ui.screens.list

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.item.Item
import com.example.preteirb.model.ItemDetails
import com.example.preteirb.model.ItemEntryViewModel
import com.example.preteirb.model.ItemUiState
import com.example.preteirb.model.ItemsOwnedUiState
import com.example.preteirb.model.ListItemsViewModel
import com.example.preteirb.model.toItemDetails
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
            }
        },
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
        NewObjectDialog(
            itemUiState = newItemUiState,
            onValueChange = onNewItemValueChange,
            onSaveObject = {
                onSaveNewItem()
                isShowObjectDialog = false
            },
            onDismissDialog = {
                isShowObjectDialog = false
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemsScreenContentPreview() {
    AppTheme {
        val fakeObjectList = listOf(
            Item(
                itemId = 1,
                name = "Item 1",
                description = "Description 1",
                userOwnerId = 1,
            ),
            Item(
                itemId = 2,
                name = "Item 2",
                description = "Description 2",
                userOwnerId = 1,
            ),
            Item(
                itemId = 3,
                name = "Item 3",
                description = "Description 3",
                userOwnerId = 1,
            ),
        )
        ListItemsScreenContent(
            objectList = ItemsOwnedUiState(itemsOwned = fakeObjectList),
            onItemClick = {},
            newItemUiState = ItemUiState(),
            onNewItemValueChange = {},
            onSaveNewItem = {},
        )
    }
}