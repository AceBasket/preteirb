package com.example.preteirb.model.items_owned

import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.user.UsersRepository
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import com.example.preteirb.model.new_usage.UsageEntryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ItemsOwnedUsageEntryViewModel @Inject constructor(
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