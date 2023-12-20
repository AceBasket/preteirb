package com.example.preteirb.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.preteirb.data.user.User
import com.example.preteirb.model.ProfileUiState
import com.example.preteirb.model.profile_selection.ProfileSelectionUiState
import com.example.preteirb.ui.screens.profile_selection.ProfileSelection
import org.junit.Rule
import org.junit.Test

class ProfileSelectionScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun noUserCreated_onlyAddProfileIsDisplayed() {
        composeTestRule.setContent {
            ProfileSelection(
                uiState = ProfileSelectionUiState(
                    users = emptyList(),
                ),
                onAddAccount = {},
                onClickOnProfile = {},
                profileUiState = ProfileUiState(),
                updateProfileUiState = {},
            )
        }

        composeTestRule
            .onNodeWithTag("addProfileCarouselItem")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("profileCarouselItem")
            .assertDoesNotExist()
    }

    @Test
    fun userCreated_onlyOneProfileAndAddProfileIsDisplayed() {
        composeTestRule.setContent {
            ProfileSelection(
                uiState = ProfileSelectionUiState(
                    users = listOf(
                        User(
                            id = 1,
                            username = "username",
                            profilePicture = null
                        )
                    ),
                ),
                onAddAccount = {},
                onClickOnProfile = {},
                profileUiState = ProfileUiState(),
                updateProfileUiState = {},
            )
        }

        composeTestRule
            .onNodeWithTag("addProfileCarouselItem")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("profileCarouselItem")
            .assertIsDisplayed()
            .assertTextContains("username")
    }

    @Test
    fun multipleUsers_onClickOnProfileWasCalled() {
        var onClickOnProfileWasCalled = false
        composeTestRule.setContent {
            ProfileSelection(
                uiState = ProfileSelectionUiState(
                    users = listOf(
                        User(
                            id = 1,
                            username = "username1",
                            profilePicture = null
                        ),
                    ),
                ),
                onAddAccount = { },
                onClickOnProfile = { onClickOnProfileWasCalled = true },
                profileUiState = ProfileUiState(),
                updateProfileUiState = {},
            )
        }

        composeTestRule
            .onNodeWithTag("profileCarouselItem")
            .performClick()
            .assert(
                SemanticsMatcher(
                    description = "onClickOnProfile was called",
                    matcher = { onClickOnProfileWasCalled })
            )

    }
}