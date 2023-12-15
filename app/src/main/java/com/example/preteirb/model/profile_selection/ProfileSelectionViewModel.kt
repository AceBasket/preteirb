package com.example.preteirb.model.profile_selection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.common.snackbar.SnackbarManager
import com.example.preteirb.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import com.example.preteirb.model.toProfileDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to retrieve all users in the Room database.
 */
@HiltViewModel
class ProfileSelectionViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private lateinit var _uiState: StateFlow<ProfileSelectionUiState>

    val uiState: StateFlow<ProfileSelectionUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            _uiState = usersRepository.getAllUsersStream()
                .map { users -> ProfileSelectionUiState(users) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = ProfileSelectionUiState(),
                )
        }
    }

    suspend fun logIn(user: User) {
        settingsRepository.storeUserId(user.id)
        settingsRepository.storeIsLoggedIn(true)
    }

    private suspend fun registerUser(user: User): Long {
        try {
            return usersRepository.insertUser(user.toProfileDetails()).id.toLong()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun registerUserAndLogIn(user: User) {
        try {
            val userId = registerUser(user).toInt()
            logIn(user.copy(id = userId))
        } catch (e: Exception) {
            Log.d("ProfileSelectionViewModel", "registerUserAndLogIn: ${e.message}")
            SnackbarManager.showMessage(e.toSnackbarMessage())
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ProfileSelectionUiState(
    val users: List<User> = emptyList(),
)