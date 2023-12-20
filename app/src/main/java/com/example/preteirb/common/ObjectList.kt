package com.example.preteirb.common

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.placeholder
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.items_owned.ItemDetails

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
            CustomGlideImage(
                model = item.image,
                placeholder = ImageVector.vectorResource(R.drawable.baseline_image_24),
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