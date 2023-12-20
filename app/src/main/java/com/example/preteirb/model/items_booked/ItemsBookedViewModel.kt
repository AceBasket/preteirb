package com.example.preteirb.model.items_booked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.common.Constants.TIMEOUT_MILLIS
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.example.preteirb.data.usage.UsageWithItemAndUser
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemsBookedViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    currentUserRepository: CurrentUserRepository,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsBookedAndNotOwnedByUser =
        currentUserRepository.currentUserFlow.flatMapLatest { currentUserInfo ->
            usersRepository.getAllItemsBookedAndNotOwnedByUserStream(currentUserInfo.user.id)
                .map { data ->
                    ItemsBookedUiState(bookings = data)
                }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemsBookedUiState()
            )
}

data class ItemsBookedUiState(
    val bookings: List<UsageWithItemAndUser> = emptyList()
)