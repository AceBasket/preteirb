package com.example.preteirb.model.items_owned

import android.net.Uri
import android.util.Log
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
open class ItemEntryViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    private val settingsRepository: SettingsRepository,
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

    open suspend fun saveItem(): Item? = saveItemInternal(true)

    protected suspend fun saveItemInternal(isNewItem: Boolean): Item? {
        val userOwnerId = settingsRepository.getUserId().first()
        updateUiState(itemUiState.itemDetails.copy(ownerId = userOwnerId))
        return if (validateInput()) {
            try {
                val newItem = if (isNewItem)
                    itemsRepository.insertItem(itemUiState.itemDetails)
                else
                    itemsRepository.updateItem(itemUiState.itemDetails)
                SnackbarManager.showMessage(R.string.save_item_success)
                newItem
            } catch (e: Exception) {
                Log.d("ItemEntryViewModel", "saveItemInternal: ${e.message}")
                Log.d("ItemEntryViewModel", "saveItemInternal: ${e.stackTrace}")
                SnackbarManager.showMessage(R.string.save_item_error)
                null
            }
        } else {
            SnackbarManager.showMessage(R.string.save_item_error)
            null
        }
    }

    fun whipeItemUiState() {
        itemUiState = ItemUiState()
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
    val image: Uri = Uri.EMPTY,
    val ownerId: Int = 0
)

fun ItemDetails.toItem(userOwnerId: Int): Item = Item(
    id = id,
    name = name,
    description = description,
    image = image.toString(),
    ownerId = userOwnerId
)

fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    description = description,
    image = Uri.parse(image ?: ""),
)