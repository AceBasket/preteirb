package com.example.preteirb.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.utils.Result
import com.example.preteirb.utils.asResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database according to filters.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    
    val uiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.length < SEARCH_QUERY_MIN_LENGTH) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                itemsRepository.getAllItemsStream()
                    .asResult()
                    .map { result ->
                        when (result) {
                            is Result.Success -> SearchResultUiState.Success(
                                itemList = result.data
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
    val itemList: List<Item> = listOf()
)