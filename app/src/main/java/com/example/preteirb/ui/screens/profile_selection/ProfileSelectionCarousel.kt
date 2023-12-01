package com.example.preteirb.ui.screens.profile_selection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.user.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileSelectionCarousel(
    list: List<User>,
    onClickOnProfile: (User) -> Unit,
    onClickOnAddProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { list.size + 1 }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 25.dp),
            modifier = Modifier.width(
                dimensionResource(id = R.dimen.image_size_large).times(
                    1.5f
                )
            )
        ) { page ->
            val itemModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))
                )
            if (page == list.size) {
                AddProfileItem(
                    modifier = Modifier.clickable { onClickOnAddProfile() }
                )
            } else {
                ProfileSelectionItem(
                    profile = list[page],
                    modifier = Modifier.clickable { onClickOnProfile(list[page]) }
                )
            }
        }
        Row {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_extra_extra_small))
                        .clip(CircleShape)
                        .background(color)
                        .size(dimensionResource(id = R.dimen.image_size_extra_extra_small))
                )
            }
        }
    }

}

@Composable
fun ProfileSelectionItem(
    profile: User,
    modifier: Modifier = Modifier
) {
    ProfileCarouselItem(
        text = profile.username,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = stringResource(
                    id = R.string.profile_image,
                    profile.username
                ),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = it
            )
        },
        modifier = modifier
    )
}

@Composable
fun AddProfileItem(
    modifier: Modifier = Modifier
) {
    ProfileCarouselItem(
        text = stringResource(id = R.string.add_profile),
        content = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_person_add_24),
                contentDescription = stringResource(
                    id = R.string.add_profile
                ),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = it
            )
        },
        modifier = modifier
    )
}

@Composable
fun ProfileCarouselItem(
    text: String,
    content: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        content(Modifier.size(dimensionResource(id = R.dimen.image_size_large)))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSelectionCarouselPreview() {
    AppTheme {
        val userList = listOf(
            User(
                userId = 0,
                username = "Sarah",
            ),
            User(
                userId = 1,
                username = "Camille",
            ),
            User(
                userId = 2,
                username = "Tiphaine",
            ),
            User(
                userId = 3,
                username = "Brigitte",
            ),
            User(
                userId = 4,
                username = "Jerome",
            )
        )
        ProfileSelectionCarousel(
            list = userList,
            onClickOnProfile = {},
            onClickOnAddProfile = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSelectionItemPreview() {
    AppTheme {
        ProfileSelectionItem(
            profile = User(
                userId = 0,
                username = "username",
            )
        )
    }
}