package com.example.preteirb.model.items_owned

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.user.UsersRepository
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ListItemsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private var _userId by Delegates.notNull<Int>()
    val userId: Int
        get() = _userId

    private val _listItemsUiState = MutableStateFlow(ItemsOwnedUiState())

    val listItemsUiState: StateFlow<ItemsOwnedUiState> get() = _listItemsUiState

    fun addItem(item: Item) {
        val currentItems = _listItemsUiState.value.itemsOwned.toMutableList()
        currentItems.add(item)
        _listItemsUiState.value = ItemsOwnedUiState(itemsOwned = currentItems)
    }

    init {
        viewModelScope.launch {
            _userId = settingsRepository.getUserId().first()
            usersRepository.getAllItemsOwnedByUserStream(_userId)
                .filterNotNull()
                .collect { itemsOwned ->
                    _listItemsUiState.value = ItemsOwnedUiState(itemsOwned = itemsOwned)
                }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}