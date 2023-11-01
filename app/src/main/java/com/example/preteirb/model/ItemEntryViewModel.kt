package com.example.preteirb.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemsRepository
import kotlinx.coroutines.flow.first

class ItemEntryViewModel(
    private val itemsRepository: ItemsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    var itemUiState by mutableStateOf(ItemUiState())
        private set
    
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }
    
    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank()
        }
    }
    
    suspend fun saveItem() {
        val userOwnerId = settingsRepository.getUserId().first()
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem(userOwnerId))
        }
    }
}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
)

fun ItemDetails.toItem(userOwnerId: Int): Item = Item(
    itemId = id,
    name = name,
    description = description,
    userOwnerId = userOwnerId
)

fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = itemId,
    name = name,
    description = description
)