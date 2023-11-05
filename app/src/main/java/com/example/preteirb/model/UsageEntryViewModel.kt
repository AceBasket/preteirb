package com.example.preteirb.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsageEntryViewModel(
    private val usagesRepository: UsagesRepository,
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    /**
     * Holds current usage ui state
     */
    var uiState by mutableStateOf(UsageUiState())
        private set
    
    private val _usagePeriodsCount = listOf(0).toMutableStateList()
    val usagePeriodsCount: List<Int>
        get() = _usagePeriodsCount
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsOwnedUiState: StateFlow<ItemsOwnedUiState> = settingsRepository
        .getUserId()
        .flatMapLatest { userId ->
            usersRepository
                .getItemsOwnedByUserStream(userId)
                .filterNotNull()
                .map { itemsOwned ->
                    ItemsOwnedUiState(itemsOwned = itemsOwned.items)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemsOwnedUiState()
        )
    
    // init with userId from settings
    init {
        viewModelScope.launch {
            uiState = UsageUiState(
                usageDetails = UsageDetails(
                    userId = settingsRepository.getUserId().first()
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
            userId > 0 && itemId > 0 && period.isNotEmpty() && period.all { it.start < it.end }
        }
    }
    
    suspend fun saveUsage() {
        if (validateInput()) {
            usagesRepository.insertUsageList(uiState.usageDetails.toUsages())
        }
    }
    
    fun addUsagePeriod() {
        _usagePeriodsCount.add(usagePeriodsCount[usagePeriodsCount.lastIndex] + 1)
    }
    
    fun deleteUsagePeriod(usagePeriodId: Int) {
        _usagePeriodsCount.remove(usagePeriodId)
    }
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class UsageUiState(
    val itemSelected: ItemDetails = ItemDetails(),
    val usageDetails: UsageDetails = UsageDetails(),
    val isEntryValid: Boolean = false
)

data class ItemsOwnedUiState(
    val itemsOwned: List<Item> = listOf()
)

data class UsageDetails(
    val usageId: Int = 0,
    val userId: Int = 0,
    val itemId: Int = 0,
    val period: MutableList<UsagePeriod> = mutableListOf()
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
