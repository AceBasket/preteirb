package com.example.preteirb.model.items_owned

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.R
import com.example.preteirb.common.snackbar.SnackbarManager
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.example.preteirb.data.cache.items_owned.ItemsOwnedRepository
import com.example.preteirb.data.cache.items_owned.toItemOwned
import com.example.preteirb.data.item.ItemDto
import com.example.preteirb.data.item.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ItemEntryViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val itemsOwnedRepository: ItemsOwnedRepository,
) : ViewModel() {
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private var userOwnerId by mutableStateOf(0)

    init {
        viewModelScope.launch {
            currentUserRepository.currentUserFlow.collect {
                userOwnerId = it.user.id
            }
        }
    }

    open fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    protected fun setImageChanged() {
        itemUiState = itemUiState.copy(isImageChanged = true)
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    open suspend fun saveItem() = saveItemInternal(true)

    protected suspend fun saveItemInternal(isNewItem: Boolean) {
        updateUiState(itemUiState.itemDetails.copy(ownerId = userOwnerId))
        if (validateInput()) {
//            try {
            if (isNewItem) {
                val newItem = itemsRepository.insertItem(itemUiState.itemDetails)
                itemsOwnedRepository.insert(newItem.toItemOwned())
            } else {
                val updatedItem =
                    if (itemUiState.isImageChanged)
                        itemsRepository.updateItem(itemUiState.itemDetails)
                    else
                        itemsRepository.updateItemName(itemUiState.itemDetails)
                itemsOwnedRepository.update(updatedItem.toItemOwned())
            }
            SnackbarManager.showMessage(R.string.save_item_success)
//            } catch (e: Exception) {
//                Log.d("ItemEntryViewModel", "saveItemInternal: ${e.message}")
//                Log.d("ItemEntryViewModel", "saveItemInternal: ${e.stackTrace}")
//                SnackbarManager.showMessage(R.string.save_item_error)
//            }
        } else {
            SnackbarManager.showMessage(R.string.save_item_error)
        }
    }

    fun whipeItemUiState() {
        itemUiState = ItemUiState()
    }
}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false,
    val isImageChanged: Boolean = false,
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: Uri = Uri.EMPTY,
    val ownerId: Int = 0
)

fun ItemDetails.toItem(userOwnerId: Int): ItemDto = ItemDto(
    id = id,
    name = name,
    description = description,
    image = image.toString(),
    ownerId = userOwnerId
)

fun ItemDto.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun ItemDto.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    description = description,
    image = Uri.parse(image ?: ""),
)