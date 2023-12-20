package com.example.preteirb.ui.screens.profile_selection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.placeholder
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.common.CustomGlideImage
import com.example.preteirb.data.user.UserDto

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileSelectionCarousel(
    list: List<UserDto>,
    onClickOnProfile: (UserDto) -> Unit,
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
            modifier = Modifier
                .width(
                    dimensionResource(id = R.dimen.image_size_large).times(
                        1.5f
                    )
                )
                .testTag("profileSelectionCarousel")
        ) { page ->
            if (page == list.size) {
                AddProfileItem(
                    modifier = Modifier
                        .clickable { onClickOnAddProfile() }
                        .testTag("addProfileCarouselItem")
                )
            } else {
                ProfileSelectionItem(
                    profile = list[page],
                    modifier = Modifier
                        .clickable { onClickOnProfile(list[page]) }
                        .testTag("profileCarouselItem")
                )
            }
        }
        Row {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.3f
                    )
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileSelectionItem(
    profile: UserDto,
    modifier: Modifier = Modifier
) {
    ProfileCarouselItem(
        text = profile.username,
        content = {
            CustomGlideImage(
                model = profile.profilePicture,
                placeholder = Icons.Default.AccountCircle,
                contentDescription = profile.username,
                loading = placeholder(R.drawable.loading_img),
                failure = placeholder(rememberVectorPainter(Icons.Default.AccountCircle)),
                contentScale = ContentScale.Crop,
                colorFilter = if (profile.profilePicture != null) null else ColorFilter.tint(
                    MaterialTheme.colorScheme.onSurface
                ),
                modifier = it
                    .clip(CircleShape)
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
        val userDtoLists = listOf(
            UserDto(
                id = 0,
                username = "Sarah",
                profilePicture = "",
            ),
            UserDto(
                id = 1,
                username = "Camille",
                profilePicture = "",
            ),
            UserDto(
                id = 2,
                username = "Tiphaine",
                profilePicture = "",
            ),
            UserDto(
                id = 3,
                username = "Brigitte",
                profilePicture = "",
            ),
            UserDto(
                id = 4,
                username = "Jerome",
                profilePicture = "",
            )
        )
        ProfileSelectionCarousel(
            list = userDtoLists,
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
            profile = UserDto(
                id = 0,
                username = "username",
                profilePicture = "",
            )
        )
    }
}