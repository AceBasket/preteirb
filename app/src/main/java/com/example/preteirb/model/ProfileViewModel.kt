package com.example.preteirb.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository

open class ProfileViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState())
        protected set

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

    protected open suspend fun saveProfile(isNewProfile: Boolean): User {
        return if (isNewProfile) {
            usersRepository.insertUser(profileUiState.profileDetails)
        } else {
            usersRepository.updateUser(profileUiState.profileDetails)
        }
    }
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