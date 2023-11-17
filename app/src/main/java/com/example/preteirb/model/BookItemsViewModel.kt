package com.example.preteirb.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.ui.screens.booking.BookItemDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookItemsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val usagesRepository: UsagesRepository,
    private val itemsRepository: ItemsRepository,
    private val settingsRepository: SettingsRepository
) : UsageEntryViewModel(usagesRepository, settingsRepository) {
    
    private val itemId: Int = checkNotNull(savedStateHandle[BookItemDestination.itemIdArg])
    
    init {
        viewModelScope.launch {
            updateUiState(
                usageDetails = UsageDetails(
                    userId = settingsRepository.getUserId().first(),
                    itemId = itemId
                )
            )
        }
    }
    
    val itemToBookDetails = itemsRepository.getItemStream(itemId)
        .filterNotNull()
        .map {
            ItemDetails(
                id = it.itemId,
                name = it.name,
                description = it.description,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemDetails()
        )
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    
}