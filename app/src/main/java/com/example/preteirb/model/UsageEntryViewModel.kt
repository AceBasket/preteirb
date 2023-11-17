package com.example.preteirb.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsagesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


abstract class UsageEntryViewModel(
    private val usagesRepository: UsagesRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
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
                )
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
            userId > 0 && itemId > 0 && period.isNotEmpty() && period.all { it.start < it.end } && period.none {
                isPeriodOverlapping(
                    it
                )
            }
        }
    }
    
    private fun isPeriodOverlapping(period: UsagePeriod): Boolean {
        return uiState.usageDetails.period.any {
            period.start < it.end && period.end > it.start && period != it
        } || uiState.bookedPeriods.any {
            period.start < it.end && period.end > it.start
        }
    }
    
    suspend fun saveUsage() {
        if (validateInput()) {
            usagesRepository.insertUsageList(uiState.usageDetails.toUsages())
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
