package com.example.preteirb.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.preteirb.data.item.Item
import com.example.preteirb.model.items_owned.ItemUiState
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import com.example.preteirb.ui.screens.items_owned.ListItemsScreenContent
import org.junit.Rule
import org.junit.Test

class ListItemsScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun emptyList_listItemsEmptyIsDisplayed() {
        composeTestRule.setContent {
            ListItemsScreenContent(
                objectList = ItemsOwnedUiState(),
                onItemClick = {},
                newItemUiState = ItemUiState(),
                onNewItemValueChange = {},
                onSaveNewItem = {},
                whipeNewItemUiState = {},
            )
        }

        composeTestRule
            .onAllNodesWithTag("objectCard")
            .assertCountEquals(0)

        composeTestRule
            .onNodeWithTag("addObjectFAB")
            .assertIsDisplayed()
    }

    @Test
    fun clickOnAddItem_addItemDialogIsDisplayed() {
        composeTestRule.setContent {
            ListItemsScreenContent(
                objectList = ItemsOwnedUiState(),
                onItemClick = {},
                newItemUiState = ItemUiState(),
                onNewItemValueChange = {},
                onSaveNewItem = {},
                whipeNewItemUiState = {},
            )
        }

        composeTestRule
            .onNodeWithTag("addObjectFAB")
            .performClick()

        composeTestRule
            .onNodeWithTag("addObjectDialog")
            .assertIsDisplayed()
    }

    @Test
    fun nonEmptyList_objectsAreDisplayed() {
        composeTestRule.setContent {
            ListItemsScreenContent(
                objectList = ItemsOwnedUiState(
                    itemsOwned = listOf(
                        Item(
                            itemId = 1,
                            name = "Item 1",
                            description = "Item 1 description",
                            userOwnerId = 1,
                        ),
                        Item(
                            itemId = 2,
                            name = "Item 2",
                            description = "Item 2 description",
                            userOwnerId = 1,
                        )
                    )
                ),
                onItemClick = {},
                newItemUiState = ItemUiState(),
                onNewItemValueChange = {},
                onSaveNewItem = { },
                whipeNewItemUiState = { }
            )
        }

        composeTestRule
            .onAllNodesWithTag("objectCard")
            .assertCountEquals(2)
    }
}