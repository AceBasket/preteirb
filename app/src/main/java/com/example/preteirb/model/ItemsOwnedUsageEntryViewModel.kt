package com.example.preteirb.model

import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ItemsOwnedUsageEntryViewModel(
    private val usersRepository: UsersRepository,
    private val usagesRepository: UsagesRepository,
    private val settingsRepository: SettingsRepository,
) : UsageEntryViewModel(usagesRepository, settingsRepository) {
    
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
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}