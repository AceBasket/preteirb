package com.example.preteirb.model.items_owned

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.ui.screens.items_owned.ItemAndUsagesDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class ItemAndUsagesDetailsViewModel @Inject constructor(
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