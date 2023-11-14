package com.example.preteirb.model

import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.flow.StateFlow

class ItemsOwnedUsageEntryViewModel(
    private val usersRepository: UsersRepository,
    private val usagesRepository: UsagesRepository,
    private val settingsRepository: SettingsRepository,
) : UsageEntryViewModel(usagesRepository, settingsRepository) {
    
    val itemsOwnedUiState: StateFlow<ItemsOwnedUiState> = getItemsOwnedUiState(
        settingsRepository = settingsRepository,
        usersRepository = usersRepository,
        coroutineScope = viewModelScope,
    )
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}