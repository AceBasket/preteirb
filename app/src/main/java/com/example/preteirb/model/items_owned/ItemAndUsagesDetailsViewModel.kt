package com.example.preteirb.model.items_owned

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
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
    private val itemsRepository: ItemsRepository,
    private val settingsRepository: SettingsRepository
) : ItemEntryViewModel(itemsRepository, settingsRepository) {

    private val itemId: Int =
        checkNotNull(savedStateHandle[ItemAndUsagesDetailsDestination.itemIdArg])

    private lateinit var _itemAndUsages: Flow<ItemAndUsages>
    val itemAndUsages: Flow<ItemAndUsages>
        get() = _itemAndUsages

    init {
        viewModelScope.launch {
            _itemAndUsages = itemsRepository.getItemAndUsagesStream(itemId).filterNotNull()
            _itemAndUsages.collect {
                updateUiState(it.getItemDetails())
            }
        }
    }

    override suspend fun saveItem(): Item? = saveItemInternal(false)


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

fun ItemAndUsages.getItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    description = description,
    image = Uri.parse(image ?: ""),
    ownerId = ownerId,
)