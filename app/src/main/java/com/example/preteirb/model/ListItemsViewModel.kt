package com.example.preteirb.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListItemsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val listItemsUiState: StateFlow<ItemsOwnedUiState> = getItemsOwnedUiState(
        usersRepository = usersRepository,
        settingsRepository = settingsRepository,
        coroutineScope = viewModelScope,
    )
}