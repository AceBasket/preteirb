package com.example.preteirb.model

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreteirbAppViewModel @Inject constructor(
    val settingsRepository: SettingsRepository,
    val usersRepository: UsersRepository
) : ViewModel() {
    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState())
        private set

    private var _currentProfile: MutableStateFlow<ProfileDetails> =
        MutableStateFlow(ProfileDetails())
    val currentProfile: StateFlow<ProfileDetails>
        get() = _currentProfile

    init {
        viewModelScope.launch {
            settingsRepository.getUserId().collect {
                usersRepository.getUserStream(it)
                    .filterNotNull()
                    .collect { user ->
                        Log.d("UserDetails (init view model)", "UserDetails: $user")
                        _currentProfile.value = user.toProfileDetails()
                        profileUiState = ProfileUiState(profileDetails = _currentProfile.value)
                        Log.d("ProfileEditer (init)", "profileUiState: $profileUiState")
                    }
            }
        }
    }

    fun updateUiState(profileDetails: ProfileDetails) {
        Log.d("updateUiState", "profileDetails: $profileDetails ; actual: $profileUiState")
        profileUiState = ProfileUiState(
            profileDetails = profileDetails,
            isEntryValid = validateInput(profileDetails),
        )
        Log.d("updateUiState", "new profileUiState: $profileUiState")
    }

    private fun validateInput(uiState: ProfileDetails = profileUiState.profileDetails): Boolean {
        return with(uiState) {
            username.isNotBlank()
        }
    }

    suspend fun saveNewProfile() {
        saveProfile(true)
    }

    suspend fun saveProfileModifications() {
        saveProfile(false)
    }

    private suspend fun saveProfile(isNewProfile: Boolean) {
        if (!validateInput()) return
        val newProfilePictureUrl = if (isNewProfile) {
            usersRepository.insertUser(profileUiState.profileDetails).profilePicture
        } else {
            usersRepository.updateUser(profileUiState.profileDetails).profilePicture
        }
        if (newProfilePictureUrl == null) return
        _currentProfile.value = ProfileDetails(
            id = _currentProfile.value.id,
            username = profileUiState.profileDetails.username,
            profilePicture = Uri.parse(newProfilePictureUrl)
        )
    }

    var profileId: Flow<Int> = settingsRepository.getUserId()
        private set

    val isLoggedIn: Flow<Boolean> = settingsRepository.getIsLoggedIn()
}

data class ProfileDetails(
    val id: Int = 0,
    val username: String = "",
    val profilePicture: Uri = Uri.EMPTY,
)

fun User.toProfileDetails() = ProfileDetails(
    id = id,
    username = username,
    profilePicture = Uri.parse(profilePicture ?: ""),
)

data class ProfileUiState(
    val profileDetails: ProfileDetails = ProfileDetails(),
    val isEntryValid: Boolean = false,
)