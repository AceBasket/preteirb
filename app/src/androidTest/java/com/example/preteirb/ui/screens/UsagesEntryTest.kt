package com.example.preteirb.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.preteirb.R
import com.example.preteirb.model.new_usage.UsageDetails
import com.example.preteirb.model.new_usage.UsagePeriod
import com.example.preteirb.model.new_usage.UsageUiState
import com.example.preteirb.ui.screens.new_usage.AddUsages
import com.example.preteirb.ui.screens.new_usage.EmptyNewUsagePeriod
import com.example.preteirb.ui.screens.new_usage.UsagePeriodListItem
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsagesEntryTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var addUsagePeriodString: String
    private lateinit var deleteUsagePeriodString: String
    private lateinit var editUsagePeriodString: String
    private lateinit var confirmEditionUsagePeriodString: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            addUsagePeriodString = getString(R.string.add_usage_period)
            deleteUsagePeriodString = getString(R.string.delete_usage_period)
            editUsagePeriodString = getString(R.string.edit_usage_period)
            confirmEditionUsagePeriodString = getString(R.string.confirm_edit_usage_period)
        }
    }

    @Test
    fun emptyUsageDetailsPeriod_onlyEmptyNewUsagePeriodIsDisplayed() {
        composeTestRule.setContent {
            AddUsages(
                usageUiState = UsageUiState(
                    usageDetails = UsageDetails(),
                    bookedPeriods = emptyList(),
                ),
                onUsageValueChange = {},
                onSaveUsageClick = {},
                validateLastUsagePeriod = { false },
            )
        }

        composeTestRule
            .onNodeWithTag("emptyNewUsagePeriod")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("usagePeriodListItem")
            .assertDoesNotExist()
    }

    @Test
    fun usageDetailsPeriodNotEmpty_usagePeriodsAreDisplayed() {
        composeTestRule.setContent {
            AddUsages(
                usageUiState = UsageUiState(
                    usageDetails = UsageDetails(
                        period = mutableStateListOf(
                            UsagePeriod(
                                start = 1,
                                end = 2,
                            ),
                            UsagePeriod(
                                start = 3,
                                end = 4,
                            ),
                        ),
                    ),
                    bookedPeriods = emptyList(),
                ),
                onUsageValueChange = {},
                onSaveUsageClick = {},
                validateLastUsagePeriod = { false },
            )
        }

        composeTestRule
            .onAllNodesWithTag("usagePeriodListItem")
            .assertCountEquals(2)

        composeTestRule
            .onNodeWithTag("emptyNewUsagePeriod")
            .assertIsDisplayed()
    }

    @Test
    fun entryValid_saveUsagePeriodsButtonIsEnabled() {
        composeTestRule.setContent {
            AddUsages(
                usageUiState = UsageUiState(
                    usageDetails = UsageDetails(),
                    bookedPeriods = emptyList(),
                    isEntryValid = true,
                ),
                onUsageValueChange = {},
                onSaveUsageClick = {},
                validateLastUsagePeriod = { true },
            )
        }

        composeTestRule
            .onNodeWithTag("saveUsagePeriodsButton")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun entryNotValid_saveUsagePeriodsButtonIsDisabled() {
        composeTestRule.setContent {
            AddUsages(
                usageUiState = UsageUiState(
                    usageDetails = UsageDetails(),
                    bookedPeriods = emptyList(),
                    isEntryValid = false,
                ),
                onUsageValueChange = {},
                onSaveUsageClick = {},
                validateLastUsagePeriod = { false },
            )
        }

        composeTestRule
            .onNodeWithTag("saveUsagePeriodsButton")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun usagePeriodAdded_oneMoreUsagePeriodIsDisplayed() {
        val periods = mutableStateListOf(
            UsagePeriod(
                start = 1,
                end = 2,
            ),
        )
        composeTestRule.setContent {
            AddUsages(
                usageUiState = UsageUiState(
                    usageDetails = UsageDetails(
                        period = periods
                    ),
                    bookedPeriods = emptyList(),
                    isEntryValid = false,
                ),
                onUsageValueChange = {},
                onSaveUsageClick = {},
                validateLastUsagePeriod = { false },
            )
        }

        composeTestRule
            .onAllNodesWithTag("usagePeriodListItem")
            .assertCountEquals(1)

        periods.add(
            UsagePeriod(
                start = 3,
                end = 4,
            )
        )

        composeTestRule
            .onAllNodesWithTag("usagePeriodListItem")
            .assertCountEquals(2)

    }

    @Test
    fun usagePeriodRemoved_oneLessUsagePeriodIsDisplayed() {
        val periods = mutableStateListOf(
            UsagePeriod(
                start = 1,
                end = 2,
            ),
            UsagePeriod(
                start = 3,
                end = 4,
            ),
        )
        composeTestRule.setContent {
            AddUsages(
                usageUiState = UsageUiState(
                    usageDetails = UsageDetails(
                        period = periods
                    ),
                    bookedPeriods = emptyList(),
                    isEntryValid = false,
                ),
                onUsageValueChange = {},
                onSaveUsageClick = {},
                validateLastUsagePeriod = { false },
            )
        }

        composeTestRule
            .onAllNodesWithTag("usagePeriodListItem")
            .assertCountEquals(2)

        periods.removeAt(0)

        composeTestRule
            .onAllNodesWithTag("usagePeriodListItem")
            .assertCountEquals(1)

    }


    /* EmptyNewUsagePeriod */
    @Test
    fun emptyNewUsagePeriod_hasAddTrailingButton() {
        composeTestRule.setContent {
            EmptyNewUsagePeriod(
                notSelectablePeriods = emptyList(),
                onNewUsagePeriodSelected = {},
                onAddUsagePeriod = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(addUsagePeriodString)
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithContentDescription(deleteUsagePeriodString)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(editUsagePeriodString)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(confirmEditionUsagePeriodString)
            .assertDoesNotExist()
    }

    @Test
    fun usagePeriodListItem_hasDeleteAndEditTrailingButtons() {
        composeTestRule.setContent {
            UsagePeriodListItem(
                usagePeriod = UsagePeriod(
                    start = 1,
                    end = 2,
                ),
                notSelectablePeriods = emptyList(),
                onNewUsagePeriodSelected = {},
                onDeleteUsagePeriod = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(addUsagePeriodString)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(deleteUsagePeriodString)
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithContentDescription(editUsagePeriodString)
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithContentDescription(confirmEditionUsagePeriodString)
            .assertDoesNotExist()
    }

    @Test
    fun emptyNewUsagePeriod_buttonsAreEnabled() {
        composeTestRule.setContent {
            EmptyNewUsagePeriod(
                notSelectablePeriods = emptyList(),
                onNewUsagePeriodSelected = {},
                onAddUsagePeriod = {}
            )
        }

        composeTestRule
            .onNodeWithTag("startUsageDateField")
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("endUsageDateField")
            .assertIsEnabled()
    }

    @Test
    fun usagePeriodListItem_buttonsAreDisabled() {
        composeTestRule.setContent {
            UsagePeriodListItem(
                usagePeriod = UsagePeriod(
                    start = 1,
                    end = 2,
                ),
                notSelectablePeriods = emptyList(),
                onNewUsagePeriodSelected = {},
                onDeleteUsagePeriod = {},
            )
        }

        composeTestRule
            .onNodeWithTag("startUsageDateField")
            .assertIsNotEnabled()

        composeTestRule
            .onNodeWithTag("endUsageDateField")
            .assertIsNotEnabled()
    }

    @Test
    fun usagePeriodListItem_clickOnEditPeriod_buttonsAreEnabled() {
        composeTestRule.setContent {
            UsagePeriodListItem(
                usagePeriod = UsagePeriod(
                    start = 1,
                    end = 2,
                ),
                notSelectablePeriods = emptyList(),
                onNewUsagePeriodSelected = {},
                onDeleteUsagePeriod = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(editUsagePeriodString)
            .performClick()

        composeTestRule
            .onNodeWithTag("startUsageDateField")
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("endUsageDateField")
            .assertIsEnabled()
    }

    @Test
    fun usagePeriodListItem_clickOnEditPeriod_clickOnConfirmEdit_buttonsAreDisabled() {
        composeTestRule.setContent {
            UsagePeriodListItem(
                usagePeriod = UsagePeriod(
                    start = 1,
                    end = 2,
                ),
                notSelectablePeriods = emptyList(),
                onNewUsagePeriodSelected = {},
                onDeleteUsagePeriod = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(editUsagePeriodString)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(confirmEditionUsagePeriodString)
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        composeTestRule
            .onNodeWithTag("startUsageDateField")
            .assertIsNotEnabled()

        composeTestRule
            .onNodeWithTag("endUsageDateField")
            .assertIsNotEnabled()
    }

    /* NewUsagePeriod */
    @Test
    fun newUsagePeriod_clickOnUsageDateField_datePickerIsDisplayed() {
        composeTestRule.setContent {
            EmptyNewUsagePeriod(
                notSelectablePeriods = emptyList(),
                onNewUsagePeriodSelected = {},
                onAddUsagePeriod = {}
            )
        }

        composeTestRule
            .onNodeWithTag("startUsageDateField")
            .performClick()

        composeTestRule
            .onNodeWithTag("usagePeriodPickerDialog")
            .assertIsDisplayed()
    }
}


