package com.example.preteirb.model.items_owned

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.item.ItemAndUsages
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemAndUsagesDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val itemId: Int =
        checkNotNull(savedStateHandle[ItemAndUsagesDetailsDestination.itemIdArg])

    lateinit private var _itemAndUsages: Flow<ItemAndUsages>
    val itemAndUsages: Flow<ItemAndUsages>
        get() = _itemAndUsages

    init {
        viewModelScope.launch {
            _itemAndUsages = itemsRepository.getItemAndUsagesStream(itemId).filterNotNull()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}