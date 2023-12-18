package com.example.preteirb.model.items_owned

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.cache.items_owned.ItemsOwnedRepository
import com.example.preteirb.data.cache.items_owned.toItemOwned
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
    private val settingsRepository: SettingsRepository,
    private val itemsOwnedRepository: ItemsOwnedRepository,
) : ViewModel() {
    private var _userId by Delegates.notNull<Int>()
    val userId: Int
        get() = _userId

    private val _listItemsUiState = MutableStateFlow(ItemsOwnedUiState())

    val listItemsUiState: StateFlow<ItemsOwnedUiState> get() = _listItemsUiState

    suspend fun editItem(newItem: Item) {
//        val currentItems = _listItemsUiState.value.itemsOwned.toMutableList()
//        val itemIndex = currentItems.indexOfFirst { it.id == newItem.id }
//        if (itemIndex == -1) return
//        currentItems[itemIndex] = newItem
//        _listItemsUiState.value = ItemsOwnedUiState(itemsOwned = currentItems)
        itemsOwnedRepository.update(newItem.toItemOwned())
    }

    init {
        viewModelScope.launch {
            _userId = settingsRepository.getUserId().first()
            usersRepository.getAllItemsOwnedByUserStream(_userId)
                .filterNotNull()
                .collect { itemsOwned ->
                    // caching items
                    itemsOwnedRepository.deleteAll()
                    itemsOwnedRepository.insertAll(itemsOwned.map { it.toItemOwned() })

                    itemsOwnedRepository.getAll()
                        .collect {
                            _listItemsUiState.value = ItemsOwnedUiState(itemsOwned = it)
                        }
                }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}