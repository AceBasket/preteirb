package com.example.preteirb.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.model.ProfileDetails
import com.example.preteirb.model.ProfileUiState
import com.example.preteirb.ui.screens.items_booked.ItemsBookedDestination
import com.example.preteirb.ui.screens.items_owned.ListItemsDestination
import com.example.preteirb.ui.screens.search.SearchDestination

enum class TopLevelDestination(
    @StringRes val titleRes: Int,
    val route: String,
    val icon: ImageVector,
) {
    SEARCH(
        titleRes = SearchDestination.titleRes,
        route = SearchDestination.route,
        icon = Icons.Rounded.Search,
    ),
    BOOKINGS(
        titleRes = ItemsBookedDestination.titleRes,
        route = ItemsBookedDestination.route,
        icon = Icons.Rounded.DateRange,
    ),
    LIST_ITEMS(
        titleRes = ListItemsDestination.titleRes,
        route = ListItemsDestination.route,
        icon = Icons.AutoMirrored.Rounded.List,
    ),
}

@Composable
fun BottomAppBar(
    destinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    isDisplayBottomAppBar: Boolean = true,
) {
    if (!isDisplayBottomAppBar) return
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination.isTopLevelDestinationInHierarchy(destination),
                onClick = { onNavigateToDestination(destination) },
                label = {
                    Text(
                        text = stringResource(id = destination.titleRes),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = "${stringResource(destination.titleRes)} Icon",
                    )
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.equals(destination.route, true) ?: false
    } ?: false

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AppTopBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToProfileSelection: () -> Unit,
    profile: State<ProfileDetails>,
    profileUiState: ProfileUiState,
    updateProfile: (ProfileDetails) -> Unit,
    onSaveChangesToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    Log.d("UserDetails (AppTopBar)", "UserDetails: $profile")
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            var showEditProfileDialog by remember { mutableStateOf(false) }
            if (showEditProfileDialog) {
                ProfileEditor(
                    uiState = profileUiState,
                    onDismissRequest = { showEditProfileDialog = false },
                    onConfirmation = {
                        showEditProfileDialog = false
                        onSaveChangesToProfile()
                    },
                    updateUiState = updateProfile,
                )
            }
            Box {
                IconButton(onClick = { isExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null,
                    )
                }
                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    Log.d("UserDetails (Dropdown Menu)", "UserDetails: $profile")
                    GlideImage(
                        model = profile.value.profilePicture,
                        contentDescription = profile.value.username,
                        loading = placeholder(R.drawable.baseline_account_circle_24),
                        failure = placeholder(R.drawable.baseline_account_circle_24),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.image_size_medium))
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                showEditProfileDialog = true
                            }
                            .clip(CircleShape),
                    )
                    Text(
                        text = profile.value.username,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_medium)))
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.logout))
                        },
                        onClick = {
                            isExpanded = false
                            navigateToProfileSelection()
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_logout_24),
                                contentDescription = stringResource(id = R.string.logout_icon)
                            )
                        }
                    )
                }
            }

        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileEditor(
    uiState: ProfileUiState,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    updateUiState: (ProfileDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d("ProfileEditer", "uiState: $uiState")
    var username by remember { mutableStateOf(uiState.profileDetails.username) }
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            shape = RoundedCornerShape(16.dp),
        ) {
            var selectedImageUri by remember {
                mutableStateOf<Uri?>(null)
            }
            Log.d(
                "ProfileEditer",
                "current image uri: ${uiState.profileDetails.profilePicture}"
            )

            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    selectedImageUri = uri
                    Log.d("ProfileEditer", "selectedImageUri: $selectedImageUri")
                    if (uri != null) {
                        updateUiState(uiState.profileDetails.copy(profilePicture = uri))
                    }
                }
            )
            Text(
                text = stringResource(id = R.string.edit_profile),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            GlideImage(
                model = selectedImageUri ?: uiState.profileDetails.profilePicture,
                contentDescription = uiState.profileDetails.username,
                loading = placeholder(R.drawable.baseline_account_circle_24),
                failure = placeholder(R.drawable.baseline_account_circle_24),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.image_size_large))
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .clip(CircleShape),
            )
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    updateUiState(uiState.profileDetails.copy(username = it))
                },
                label = { Text(text = stringResource(id = R.string.username)) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                    )
                }
                TextButton(onClick = onConfirmation) {
                    Text(
                        text = stringResource(id = R.string.edit),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditerPreview() {
    AppTheme {
        ProfileEditor(
            uiState = ProfileUiState(
                profileDetails = ProfileDetails(),
            ),
            onDismissRequest = {},
            onConfirmation = {},
            updateUiState = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomAppBarPreview() {
    AppTheme {
        BottomAppBar(
            destinations = TopLevelDestination.entries,
            currentDestination = NavDestination("search"),
            onNavigateToDestination = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    AppTheme {
        AppTopBar(
            currentScreenTitle = R.string.app_name,
            canNavigateBack = false,
            navigateUp = {},
            navigateToProfileSelection = {},
            profileUiState = ProfileUiState(),
            updateProfile = {},
            onSaveChangesToProfile = {},
            profile = remember { mutableStateOf(ProfileDetails()) },
        )
    }
}