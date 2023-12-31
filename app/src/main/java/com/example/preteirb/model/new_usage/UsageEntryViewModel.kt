package com.example.preteirb.model.new_usage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.R
import com.example.preteirb.common.snackbar.SnackbarManager
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.example.preteirb.data.cache.items_owned.ItemOwned
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.usage.toUsage
import com.example.preteirb.ui.screens.booking.BookItemDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


abstract class UsageEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val usagesRepository: UsagesRepository,
    private val currentUserRepository: CurrentUserRepository,
) : ViewModel() {

    protected val itemId: Int = checkNotNull(savedStateHandle[BookItemDestination.itemIdArg])

    /**
     * Holds current usage ui state
     */
    var uiState by mutableStateOf(UsageUiState())
        private set

    init {
        viewModelScope.launch {
            val bookedPeriods = usagesRepository.getAllUsagesByItemIdStream(itemId)
                .first().usages.map { it.toUsage().toUsagePeriod() }
            currentUserRepository.currentUserFlow.collect {
                uiState = uiState.copy(
                    usageDetails = uiState.usageDetails.copy(
                        userId = it.user.id,
                        itemId = itemId
                    ),
                    bookedPeriods = bookedPeriods
                )
            }
        }
    }

    /**
     * Updates the [uiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(usageDetails: UsageDetails) {
        uiState =
            UsageUiState(
                usageDetails = usageDetails,
                isEntryValid = validateInput(usageDetails),
                bookedPeriods = uiState.bookedPeriods
            )
    }

    private fun validateInput(uiState: UsageDetails = this.uiState.usageDetails): Boolean {
        return with(uiState) {
            userId > 0 && itemId > 0 && validatePeriods(period)
        }
    }

    private fun validatePeriods(periods: List<UsagePeriod>): Boolean {
        return periods.isNotEmpty() && periods.all { it.start <= it.end } && periods.none {
            isPeriodOverlapping(it) || it.start < System.currentTimeMillis() || it.end < System.currentTimeMillis()
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
        // add last period to the list of periods
        if (lastPeriod != null) {
            val newPeriods = uiState.usageDetails.period.toMutableList()
            newPeriods.add(lastPeriod)
            updateUiState(uiState.usageDetails.copy(period = newPeriods.toMutableStateList()))

            // check if the new list of periods is valid
            if (!uiState.isEntryValid) {
                return
            }
        }
        try {
            usagesRepository.insertUsageList(
                uiState.usageDetails.toUsages()
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
    val itemsOwned: List<ItemOwned> = listOf()
)

data class UsageDetails(
    val usageId: Int = 0,
    val userId: Int = 0,
    val itemId: Int = 0,
    val period: List<UsagePeriod> = mutableStateListOf(),
)

data class UsagePeriod(
    val start: Long,
    val end: Long,
)

fun UsageDetails.toUsages(): List<Usage> = period.map { usagePeriod ->
    Usage(
        id = usageId,
        userUsingItemId = userId,
        itemUsedId = itemId,
        startDateTime = usagePeriod.start,
        endDateTime = usagePeriod.end,
    )
}

fun Usage.toUsagePeriod(): UsagePeriod = UsagePeriod(
    start = startDateTime,
    end = endDateTime,
)
