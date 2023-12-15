package com.example.preteirb.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreteirbAppViewModel @Inject constructor(
    val settingsRepository: SettingsRepository,
    val usersRepository: UsersRepository
) : ViewModel() {

    var profileUiState: ProfileUiState = ProfileUiState()
        private set

    init {
        viewModelScope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            profileUiState =
                settingsRepository.getUserId()
                    .flatMapLatest { usersRepository.getUserStream(it) }
                    .filterNotNull()
                    .map {
                        ProfileUiState(
                            profileDetails = it.toProfileDetails(),
                        )
                    }
                    .first()
        }
    }

    fun updateUiState(profileDetails: ProfileDetails) {
        profileUiState = ProfileUiState(
            profileDetails = profileDetails,
            isEntryValid = validateInput(profileDetails),
        )
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
        if (isNewProfile) {
            usersRepository.insertUser(profileUiState.profileDetails)
        } else {
            usersRepository.updateUser(profileUiState.profileDetails)
        }
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