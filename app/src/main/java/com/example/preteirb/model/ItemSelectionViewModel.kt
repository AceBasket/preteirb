package com.example.preteirb.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemSelectionViewModel @Inject constructor(private val itemsRepository: ItemsRepository) : ViewModel() {
    val uiState: StateFlow<ItemSelectionUiState> =
        itemsRepository.getAllItemsStream().map { ItemSelectionUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemSelectionUiState()
            )
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for item selection dropdown menu in [UsageEntryScreen]
 */
data class ItemSelectionUiState(val itemList: List<Item> = listOf())
