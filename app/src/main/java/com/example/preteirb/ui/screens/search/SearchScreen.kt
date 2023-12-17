package com.example.preteirb.ui.screens.search

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.items_owned.ItemDetails
import com.example.preteirb.model.search.SearchResultUiState
import com.example.preteirb.model.search.SearchViewModel
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
            SearchResultUiState.Loading,
            SearchResultUiState.LoadFailed,
            SearchResultUiState.EmptyQuery,
            -> Unit

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


@Composable
fun ObjectList(
    objects: List<ItemDetails>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = objects) {
            ObjectCard(
                item = it,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_small),
                    )
                    .clickable { onItemClick(it.id) }
                    .testTag("objectCard")
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
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
            GlideImage(
                model = if (item.image == Uri.EMPTY) R.drawable.baseline_image_24 else item.image,
                contentDescription = item.name,
                loading = placeholder(R.drawable.loading_img),
                failure = placeholder(R.drawable.ic_broken_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.image_size_medium))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_small)))

            )
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_small)))
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