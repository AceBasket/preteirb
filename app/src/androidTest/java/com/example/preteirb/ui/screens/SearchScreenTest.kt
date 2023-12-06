package com.example.preteirb.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.search.SearchResultUiState
import com.example.preteirb.ui.screens.search.SearchScreenContent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Search screen.
 */
class SearchScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var noResultString: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            noResultString = "Sorry, there is no content found for your search"
        }
    }

    @Test
    fun emptySearchResult_noResultScreenIsDisplayed() {
        composeTestRule.setContent {
            SearchScreenContent(
                uiState = SearchResultUiState.Success(emptyList()),
                queryString = "esug",
                onSearchQueryChange = {},
                navigateToBookItem = {},
            )
        }

        composeTestRule
            .onNodeWithText(noResultString, substring = true)
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithTag("objectCard")
            .assertCountEquals(0)

    }

    @Test
    fun searchResultsExist_itemsAreDisplayed() {
        composeTestRule.setContent {
            SearchScreenContent(
                uiState = SearchResultUiState.Success(
                    listOf(
                        ItemDetails(
                            id = 1,
                            name = "Item 1",
                            description = "Item 1 description",
                        ),
                        ItemDetails(
                            id = 2,
                            name = "Item 2",
                            description = "Item 2 description",
                        )
                    )
                ),
                queryString = "ite",
                onSearchQueryChange = {},
                navigateToBookItem = {},
            )
        }

        composeTestRule
            .onAllNodesWithTag("objectCard")
            .assertCountEquals(2)
    }
}