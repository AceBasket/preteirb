package com.example.preteirb.model

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.preteirb.CurrentUser
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreteirbAppViewModel @Inject constructor(
    usersRepository: UsersRepository,
    private val currentUserRepository: CurrentUserRepository,
) : ProfileViewModel(usersRepository) {
    var currentProfile: CurrentUser by mutableStateOf(CurrentUser.getDefaultInstance())
        private set

    var isLoggedIn: Boolean by mutableStateOf(false)
        private set

    var isProfileSelected: Boolean by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            currentUserRepository.currentUserFlow.collect {
                Log.d("currentUserRepo", "currentUserFlow.collect: $it")
                isLoggedIn = it.loggedIn
                isProfileSelected = it.profileSelected
                if (isLoggedIn && isProfileSelected) {
                    currentProfile = it.user
                    updateUiState(it.user.toProfileDetails())
                }
            }
        }
    }

    suspend fun logOut() {
        currentUserRepository.setIsProfileSelected(false)
        currentUserRepository.clearCurrentUser()
    }

    suspend fun saveProfileModifications() {
        val changedProfile = saveProfile(false)
        currentUserRepository.setCurrentUser(changedProfile)
    }

    override suspend fun saveProfile(isNewProfile: Boolean): User {
        val newUser = super.saveProfile(isNewProfile)
        currentUserRepository.setCurrentUser(newUser)
        return newUser
    }
}

fun CurrentUser.toProfileDetails(): ProfileDetails {
    return ProfileDetails(
        id = this.id,
        username = this.username,
        profilePicture = Uri.parse(this.profilePic),
    )
}