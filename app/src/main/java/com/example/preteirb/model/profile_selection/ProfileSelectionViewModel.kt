package com.example.preteirb.model.profile_selection

import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import com.example.preteirb.model.ProfileViewModel
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
    private val settingsRepository: SettingsRepository,
    private val usersRepository: UsersRepository,
) : ProfileViewModel(settingsRepository, usersRepository) {
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

    suspend fun saveNewProfile(): User {
        return saveProfile(true)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ProfileSelectionUiState(
    val users: List<User> = emptyList(),
)