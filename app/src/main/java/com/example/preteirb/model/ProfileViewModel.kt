package com.example.preteirb.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.user.UserDto
import com.example.preteirb.data.user.UsersRepository

open class ProfileViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState())
        protected set

    open fun updateUiState(profileDetails: ProfileDetails) {
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

    open suspend fun saveProfile(isNewProfile: Boolean): UserDto {
        return if (isNewProfile) {
            usersRepository.insertUser(profileUiState.profileDetails)
        } else if (!profileUiState.isImageChanged) {
            usersRepository.updateUsername(profileUiState.profileDetails)
        } else {
            usersRepository.updateUser(profileUiState.profileDetails)
        }
    }

    protected fun setImageChanged() {
        profileUiState = profileUiState.copy(isImageChanged = true)
    }
}

data class ProfileDetails(
    val id: Int = 0,
    val username: String = "",
    val profilePicture: Uri = Uri.EMPTY,
)

fun UserDto.toProfileDetails() = ProfileDetails(
    id = id,
    username = username,
    profilePicture = Uri.parse(profilePicture ?: ""),
)

data class ProfileUiState(
    val profileDetails: ProfileDetails = ProfileDetails(),
    val isEntryValid: Boolean = false,
    val isImageChanged: Boolean = false,
)