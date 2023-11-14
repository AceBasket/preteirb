package com.example.preteirb.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.ui.screens.list.ItemAndUsagesDetailsDestination
import kotlinx.coroutines.flow.filterNotNull

class ItemAndUsagesDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    
    private val itemId: Int =
        checkNotNull(savedStateHandle[ItemAndUsagesDetailsDestination.itemIdArg])
    
    val itemAndUsages = itemsRepository.getItemAndUsagesStream(itemId).filterNotNull()
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    
}