package com.example.preteirb.model.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.model.items_owned.ItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel to retrieve all items in the Room database according to filters.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    val uiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.length < SEARCH_QUERY_MIN_LENGTH) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                itemsRepository.getItemsFromQueryStream(searchQuery.value)
                    .asResult()
                    .map { result ->
                        when (result) {
                            is Result.Success -> SearchResultUiState.Success(
                                // transform list of items to list of item details
                                itemList = result.data.map { item ->
                                    ItemDetails(
                                        id = item.id,
                                        name = item.name,
                                        description = item.description,
                                    )
                                }
                            )

                            is Result.Loading -> SearchResultUiState.Loading
                            is Result.Error -> SearchResultUiState.LoadFailed
                        }
                    }


            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchResultUiState.Loading,
        )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/** Minimum length where search query is considered as [SearchResultUiState.EmptyQuery] */
private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_QUERY = "searchQuery"

data class SearchResultsUiState(
    val itemList: List<ItemDetails> = listOf()
)