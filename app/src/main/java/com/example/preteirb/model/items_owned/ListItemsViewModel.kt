package com.example.preteirb.model.items_owned

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.UsersRepository
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ListItemsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val listItemsUiState: StateFlow<ItemsOwnedUiState> = settingsRepository
        .getUserId()
        .flatMapLatest { userId ->
            usersRepository
                .getAllItemsOwnedByUserStream(userId)
                .filterNotNull()
                .map { itemsOwned ->
                    ItemsOwnedUiState(itemsOwned = itemsOwned)
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