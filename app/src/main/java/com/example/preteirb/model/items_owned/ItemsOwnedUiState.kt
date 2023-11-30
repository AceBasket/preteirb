package com.example.preteirb.model.items_owned

import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.UsersRepository
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
fun getItemsOwnedUiState(
    settingsRepository: SettingsRepository,
    usersRepository: UsersRepository,
    coroutineScope: CoroutineScope
): StateFlow<ItemsOwnedUiState> {
    val TIMEOUT_MILLIS = 5_000L
    return settingsRepository
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
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemsOwnedUiState()
        )
}