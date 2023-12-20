package com.example.preteirb.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.common.ObjectList
import com.example.preteirb.model.search.SearchResultUiState
import com.example.preteirb.model.search.SearchViewModel
import com.example.preteirb.ui.navigation.NavigationDestination

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.find_object
}

@Composable
fun SearchScreen(
    navigateToBookItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    // this is the text users enter
    val queryString by viewModel.searchQuery.collectAsStateWithLifecycle()

    SearchScreenContent(
        uiState = uiState.value,
        queryString = queryString,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        navigateToBookItem = navigateToBookItem,
        modifier = modifier.testTag("searchScreen")
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: SearchResultUiState,
    queryString: String,
    onSearchQueryChange: (String) -> Unit,
    navigateToBookItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // if the search bar is active or not
    var isActive by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
    ) {
        SearchBar(
            query = queryString,
            onQueryChange = onSearchQueryChange,
            onSearch = { isActive = false },
            active = isActive,
            onActiveChange = {},
            placeholder = { Text(text = stringResource(id = R.string.search)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .testTag("searchTextField")
        ) {}

        when (uiState) {
            SearchResultUiState.LoadFailed,
            SearchResultUiState.EmptyQuery,
            -> Unit

            is SearchResultUiState.Loading -> {
                CircularProgressIndicator()
            }

            is SearchResultUiState.Success -> {
                if (uiState.isEmpty()) {
                    Text(text = stringResource(id = R.string.search_result_not_found, queryString))
                } else {
                    val items = uiState.itemList
                    ObjectList(objects = items, onItemClick = navigateToBookItem)
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    AppTheme {
        SearchScreen(navigateToBookItem = {})
    }
}