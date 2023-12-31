package com.example.preteirb.model.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.preteirb.common.Constants.TIMEOUT_MILLIS
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.new_usage.UsageEntryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookItemsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    usagesRepository: UsagesRepository,
    currentUserRepository: CurrentUserRepository,
) : UsageEntryViewModel(savedStateHandle, usagesRepository, currentUserRepository) {

    private lateinit var _itemToBookDetails: StateFlow<ItemDetails>
    val itemToBookDetails: StateFlow<ItemDetails>
        get() = _itemToBookDetails

    init {
        viewModelScope.launch {
            _itemToBookDetails = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .map {
                    ItemDetails(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = ItemDetails()
                )
        }
    }
}