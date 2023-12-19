package com.example.preteirb.model.items_owned

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.example.preteirb.data.cache.items_owned.ItemsOwnedRepository
import com.example.preteirb.data.cache.items_owned.toItemOwned
import com.example.preteirb.data.user.UsersRepository
import com.example.preteirb.model.new_usage.ItemsOwnedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListItemsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val itemsOwnedRepository: ItemsOwnedRepository,
) : ViewModel() {
    var userId by mutableStateOf(0)
        private set

    private val _listItemsUiState = MutableStateFlow(ItemsOwnedUiState())

    val listItemsUiState: StateFlow<ItemsOwnedUiState>
        get() = _listItemsUiState

    init {
        viewModelScope.launch {
            currentUserRepository.currentUserFlow.collect { currentUserInfo ->
                userId = currentUserInfo.user.id
                usersRepository.getAllItemsOwnedByUserStream(userId)
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
    }
}