package com.example.preteirb.model.items_booked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.usage.UsageWithItemAndUser
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemsBookedViewModel @Inject constructor(
    val usersRepository: UsersRepository,
    val settingsRepository: SettingsRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsBookedAndNotOwnedByUser = settingsRepository
        .getUserId()
        .flatMapLatest { userId ->
            usersRepository
                .getAllItemsBookedAndNotOwnedByUserStream(userId)
                .filterNotNull()
                .map { data ->
                    ItemsBookedUiState(bookings = data)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemsBookedUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ItemsBookedUiState(
    val bookings: List<UsageWithItemAndUser> = emptyList()
)