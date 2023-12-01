package com.example.preteirb.model.items_owned

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.R
import com.example.preteirb.common.snackbar.SnackbarManager
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ItemEntryViewModel @Inject constructor(
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
            name.isNotBlank()
        }
    }

    suspend fun saveItem() {
        val userOwnerId = settingsRepository.getUserId().first()
        if (validateInput()) {
            try {
                itemsRepository.insertItem(itemUiState.itemDetails.toItem(userOwnerId))
                SnackbarManager.showMessage(R.string.save_item_success)
            } catch (e: Exception) {
                SnackbarManager.showMessage(R.string.save_item_error)
            }
        } else {
            SnackbarManager.showMessage(R.string.save_item_error)
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