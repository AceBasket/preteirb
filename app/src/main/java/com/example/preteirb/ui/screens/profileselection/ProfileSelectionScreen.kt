package com.example.preteirb.ui.screens.profileselection

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
            //pageSpacing = 0.dp,
            contentPadding = PaddingValues(horizontal = 25.dp),
            modifier = Modifier.width(
                dimensionResource(id = R.dimen.big_profile_image_size).times(
                    1.5f
                )
            )
        ) { page ->
            val itemModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
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
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
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
        contentDescription = stringResource(id = R.string.profile_image, profile.username),
        image = R.drawable.baseline_account_circle_24,
        modifier = modifier
    )
}

@Composable
fun AddProfileItem(
    modifier: Modifier = Modifier
) {
    ProfileCarouselItem(
        text = stringResource(id = R.string.add_profile),
        contentDescription = stringResource(
            id = R.string.add_profile
        ),
        image = R.drawable.baseline_person_add_24,
        modifier = modifier
    )
}

@Composable
fun ProfileCarouselItem(
    text: String,
    contentDescription: String,
    @DrawableRes image: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.big_profile_image_size))
                //.clip(CircleShape)
        )
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
                location = "location",
            ),
            User(
                userId = 1,
                username = "Camille",
                location = "location",
            ),
            User(
                userId = 2,
                username = "Tiphaine",
                location = "location",
            ),
            User(
                userId = 3,
                username = "Brigitte",
                location = "location",
            ),
            User(
                userId = 4,
                username = "Jerome",
                location = "location",
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
                location = "location",
            )
        )
    }
}