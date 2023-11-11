package com.example.preteirb.model

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    
    /**
     * The state query is empty or too short. To distinguish the state between the
     * (initial state or when the search query is cleared) vs the state where no search
     * result is returned, explicitly define the empty query state.
     */
    data object EmptyQuery : SearchResultUiState
    data object LoadFailed : SearchResultUiState
    data class Success(val itemList: List<ItemDetails> = emptyList()) : SearchResultUiState {
        fun isEmpty() = itemList.isEmpty()
    }
}