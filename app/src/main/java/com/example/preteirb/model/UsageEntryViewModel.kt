package com.example.preteirb.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.DateFormat

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
        settingsRepository.getUserId().map {
            updateUiState(uiState.usageDetails.copy(userId = it))
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
            userId > 0 && itemId > 0 && period.isNotEmpty() && period.all { it.first < it.second }
        }
    }
    
    suspend fun saveUsage() {
        if (validateInput()) {
            usagesRepository.insertUsageList(uiState.usageDetails.toUsages())
        }
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
    val userId: Int = 0,
    val itemId: Int = 0,
    val period: List<Pair<Long, Long>> = emptyList()
)

fun UsageDetails.toUsages(): List<Usage> = period.map {
    Usage(
        userId = userId,
        itemId = itemId,
        startDateTime = DateFormat.getDateTimeInstance().format(it.first),
        endDateTime = DateFormat.getDateTimeInstance().format(it.second)
    )
}
