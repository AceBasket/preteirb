package com.example.preteirb.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsagesRepository
import java.text.DateFormat

class UsageEntryViewModel(
    private val usagesRepository: UsagesRepository
) : ViewModel() {
    /**
     * Holds current usage ui state
     */
    var uiState by mutableStateOf(UsageUiState())
        private set
    
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
            userId != 0 && itemId != 0 && period.isNotEmpty() && period.all { it.first < it.second }
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
    val isEntryValid: Boolean = false
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
