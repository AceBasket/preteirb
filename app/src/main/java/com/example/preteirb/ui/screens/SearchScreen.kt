package com.example.preteirb.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.preteirb.R


data class Item(
    val name: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    // this is the text users enter
    var queryString by remember {
        mutableStateOf("")
    }
    
    // if the search bar is active or not
    var isActive by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
    ) {
        SearchBar(
            query = queryString,
            onQueryChange = {},
            onSearch = {},
            active = isActive,
            onActiveChange = {},
            placeholder = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_filter_list_24),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
        }
        ObjectList(
            objects = listOf(
                Item("Tondeuse", "Tondeuse à gazon"),
                Item("Pelle", "Grosse pelle"),
                Item("Rateau", "Evitez le rateau"),
            )
        )
    }
}

@Composable
fun ObjectList(objects: List<Item>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        //verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(
            items = objects,
            key = {
                it.name
            }
        ) {
            ObjectCard(
                item = it,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_small),
                    )
            )
        }
    }
}

@Composable
fun ObjectCard(item: Item, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
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
            Item("Tondeuse", "Tondeuse à gazon"),
            Item("Pelle", "Grosse pelle"),
            Item("Rateau", "Evitez le rateau"),
        )
        ObjectList(fakeItems)
    }
}

@Preview
@Composable
fun ObjectCardPreview() {
    AppTheme {
        val item = Item("Tondeuse", "Tondeuse à gazon")
        ObjectCard(item)
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    AppTheme {
        SearchScreen()
    }
}