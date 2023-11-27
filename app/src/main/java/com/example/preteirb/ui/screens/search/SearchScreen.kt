package com.example.preteirb.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.AppViewModelProvider
import com.example.preteirb.model.ItemDetails
import com.example.preteirb.model.SearchResultUiState
import com.example.preteirb.model.SearchViewModel
import com.example.preteirb.ui.navigation.NavigationDestination

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.find_object
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToBookItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    //viewModel: SearchViewModel = hiltViewModel(),
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val uiState = viewModel.uiState.collectAsState()

    // this is the text users enter
    val queryString by viewModel.searchQuery.collectAsStateWithLifecycle()

    // if the search bar is active or not
    var isActive by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
    ) {
        SearchBar(
            query = queryString,
            onQueryChange = viewModel::onSearchQueryChanged,
            onSearch = { isActive = false },
            active = isActive,
            onActiveChange = {},
            placeholder = { Text(text = stringResource(id = R.string.search)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {}

        when (uiState.value) {
            SearchResultUiState.Loading,
            SearchResultUiState.LoadFailed,
            SearchResultUiState.EmptyQuery,
            -> Unit

            is SearchResultUiState.Success -> {
                if ((uiState.value as SearchResultUiState.Success).isEmpty()) {
                    Text(text = stringResource(id = R.string.search_result_not_found, queryString))
                } else {
                    val items = (uiState.value as SearchResultUiState.Success).itemList
                    ObjectList(objects = items, onItemClick = navigateToBookItem)
                }
            }

        }

        //FloatingActionButton(
        //    onClick = { /* TODO */ },
        //) {
        //    Icon(painterResource(id = R.drawable.baseline_filter_list_24), "Floating action button.")
        //}
    }
}


@Composable
fun ObjectList(
    objects: List<ItemDetails>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        //verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(
            items = objects,
            key = {
                it.id
            }
        ) {
            ObjectCard(
                item = it,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_small),
                    )
                    .clickable { onItemClick(it.id) }
            )
        }
    }
}

@Composable
fun ObjectCard(
    item: ItemDetails,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.image_size_medium))
            )
            Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun ObjectListPreview() {
    AppTheme {
        val fakeItems = listOf(
            ItemDetails(1, "Tondeuse", "Tondeuse à gazon"),
            ItemDetails(2, "Pelle", "Grosse pelle"),
            ItemDetails(3, "Rateau", "Evitez le rateau"),
        )
        ObjectList(fakeItems, onItemClick = {})
    }
}

@Preview
@Composable
fun ObjectCardPreview() {
    AppTheme {
        val item = ItemDetails(1, "Tondeuse", "Tondeuse à gazon")
        ObjectCard(item)
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    AppTheme {
        SearchScreen(navigateToBookItem = {})
    }
}