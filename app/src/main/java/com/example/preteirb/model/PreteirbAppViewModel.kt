package com.example.preteirb.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreteirbAppViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val usersRepository: UsersRepository
) : ProfileViewModel(settingsRepository, usersRepository) {
    var currentProfile: ProfileDetails by mutableStateOf(ProfileDetails())
        private set

    init {
        viewModelScope.launch {
            settingsRepository.getUserId().collect {
                usersRepository.getUserStream(it)
                    .filterNotNull()
                    .collect { user ->
                        currentProfile = user.toProfileDetails()
                        profileUiState = ProfileUiState(profileDetails = currentProfile)
                    }

            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            settingsRepository.storeIsLoggedIn(false)
        }
    }

    suspend fun logIn(user: User) {
        settingsRepository.storeUserId(user.id)
        settingsRepository.storeIsLoggedIn(true)
        currentProfile = user.toProfileDetails()
        profileUiState = profileUiState.copy(profileDetails = currentProfile)

    }

    suspend fun saveProfileModifications() {
        saveProfile(false)
    }

    override suspend fun saveProfile(isNewProfile: Boolean): User {
        val newUser = super.saveProfile(isNewProfile)
        currentProfile = newUser.toProfileDetails()
        return newUser
    }

    var profileId: Flow<Int> = settingsRepository.getUserId()
        private set

    val isLoggedIn: Flow<Boolean> = settingsRepository.getIsLoggedIn()
}