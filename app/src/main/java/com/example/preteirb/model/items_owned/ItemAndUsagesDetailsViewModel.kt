package com.example.preteirb.model.items_owned

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.example.preteirb.data.cache.items_owned.ItemsOwnedRepository
import com.example.preteirb.data.item.ItemAndUsagesDto
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemAndUsagesDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    currentUserRepository: CurrentUserRepository,
    itemsOwnedRepository: ItemsOwnedRepository,
) : ItemEntryViewModel(itemsRepository, currentUserRepository, itemsOwnedRepository) {

    private val itemId: Int =
        checkNotNull(savedStateHandle[ItemAndUsagesDetailsDestination.itemIdArg])

    private lateinit var _itemAndUsagesDto: StateFlow<ItemAndUsagesDto>
    private var itemImage: Uri = Uri.EMPTY
    val itemAndUsagesDto: StateFlow<ItemAndUsagesDto>
        get() = _itemAndUsagesDto

    init {
        viewModelScope.launch {
            _itemAndUsagesDto =
                itemsRepository.getItemAndUsagesStream(itemId).filterNotNull().stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = ItemAndUsagesDto(
                        id = 0,
                        name = "",
                        description = "",
                        image = null,
                        ownerId = 0,
                        usages = listOf()
                    )
                )
            _itemAndUsagesDto.collect {
                updateUiState(it.getItemDetails())
                itemImage = itemAndUsagesDto.first().getItemDetails().image
            }
        }
    }

    override suspend fun saveItem() = saveItemInternal(false)

    override fun updateUiState(itemDetails: ItemDetails) {
        if (itemDetails.image != itemImage) {
            setImageChanged()
        }
        super.updateUiState(itemDetails)
    }
}

fun ItemAndUsagesDto.getItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    description = description,
    image = Uri.parse(image ?: ""),
    ownerId = ownerId,
)