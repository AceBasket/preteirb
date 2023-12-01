package com.example.preteirb.model.new_usage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.R
import com.example.preteirb.common.snackbar.SnackbarManager
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.ui.screens.booking.BookItemDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


abstract class UsageEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val usagesRepository: UsagesRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    protected val itemId: Int = checkNotNull(savedStateHandle[BookItemDestination.itemIdArg])

    /**
     * Holds current usage ui state
     */
    var uiState by mutableStateOf(UsageUiState())
        private set

    // init with userId from settings
    init {
        viewModelScope.launch {
            uiState = UsageUiState(
                usageDetails = UsageDetails(
                    userId = settingsRepository.getUserId().first(),
                    itemId = itemId
                ),
                bookedPeriods = usagesRepository.getAllUsagesByItemIdStream(itemId).first()
                    .map { it.toUsagePeriod() }
            )
        }
    }

    /**
     * Updates the [uiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(usageDetails: UsageDetails) {
        uiState =
            UsageUiState(usageDetails = usageDetails, isEntryValid = validateInput(usageDetails))
    }

    private fun validateInput(uiState: UsageDetails = this.uiState.usageDetails): Boolean {
        return with(uiState) {
            userId > 0 && itemId > 0 && validatePeriods(period)
        }
    }

    fun validatePeriods(periods: List<UsagePeriod>): Boolean {
        return periods.isNotEmpty() && periods.all { it.start < it.end } && periods.none {
            isPeriodOverlapping(
                it
            )
        }
    }

    fun validateLastPeriod(period: UsagePeriod): Boolean {
        return if (period.start < period.end && !isPeriodOverlapping(period)) {
            uiState = uiState.copy(isEntryValid = true)
            true
        } else {
            uiState = uiState.copy(isEntryValid = false)
            false
        }
    }

    private fun isPeriodOverlapping(period: UsagePeriod): Boolean {
        return uiState.usageDetails.period.any {
            period.start < it.end && period.end > it.start && period != it
        } || uiState.bookedPeriods.any {
            period.start < it.end && period.end > it.start
        }
    }

    suspend fun saveUsage(lastPeriod: UsagePeriod?) {
        if (!uiState.isEntryValid) {
            return
        }
        val newPeriods = uiState.usageDetails.period.toMutableList()
        newPeriods.add(lastPeriod ?: return)
        try {
            usagesRepository.insertUsageList(
                uiState.usageDetails.copy(period = newPeriods.toMutableStateList()).toUsages()
            )
            SnackbarManager.showMessage(R.string.save_usages_success)
        } catch (e: Exception) {
            SnackbarManager.showMessage(R.string.save_usages_error)
            return
        }
    }
}

data class UsageUiState(
    val usageDetails: UsageDetails = UsageDetails(),
    val bookedPeriods: List<UsagePeriod> = listOf(),
    val isEntryValid: Boolean = false
)

data class ItemsOwnedUiState(
    val itemsOwned: List<Item> = listOf()
)

data class UsageDetails(
    val usageId: Int = 0,
    val userId: Int = 0,
    val itemId: Int = 0,
    val period: SnapshotStateList<UsagePeriod> = mutableStateListOf(),
)

data class UsagePeriod(
    val start: Long,
    val end: Long,
)

fun UsageDetails.toUsages(): List<Usage> = period.map { usagePeriod ->
    Usage(
        usageId = usageId,
        userId = userId,
        itemId = itemId,
        //TODO: might need to change this (0 would be a really bad value)
        startDateTime = usagePeriod.start,
        endDateTime = usagePeriod.end,
    )
}

fun Usage.toUsagePeriod(): UsagePeriod = UsagePeriod(
    start = startDateTime,
    end = endDateTime,
)
