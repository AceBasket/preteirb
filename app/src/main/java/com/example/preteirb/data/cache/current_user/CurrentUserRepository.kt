package com.example.preteirb.data.cache.current_user

import androidx.datastore.core.DataStore
import com.example.preteirb.CurrentUser
import com.example.preteirb.CurrentUserInfo
import com.example.preteirb.data.user.UserDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserRepository @Inject constructor(private val currentUserInfoDataStore: DataStore<CurrentUserInfo>) {
    val currentUserFlow: Flow<CurrentUserInfo> = currentUserInfoDataStore.data

    suspend fun setCurrentUser(userDto: UserDto) {
        currentUserInfoDataStore.updateData {
            val currentUser = CurrentUser.newBuilder()
                .setId(userDto.id)
                .setProfilePic(userDto.profilePicture ?: "")
                .setUsername(userDto.username)
                .build()
            it.toBuilder().setUser(currentUser).build()
        }
    }

    suspend fun clearCurrentUser() {
        this.currentUserInfoDataStore.updateData {
            it.toBuilder().setUser(CurrentUser.getDefaultInstance()).build()
        }
    }

    suspend fun setToken(token: String) {
        currentUserInfoDataStore.updateData {
            it.toBuilder().setToken(token).build()
        }
    }

    suspend fun clearToken() {
        currentUserInfoDataStore.updateData {
            it.toBuilder().setToken("").build()
        }
    }

    suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        currentUserInfoDataStore.updateData {
            it.toBuilder().setLoggedIn(isLoggedIn).build()
        }
    }

    suspend fun setIsProfileSelected(isProfileSelected: Boolean) {
        currentUserInfoDataStore.updateData {
            it.toBuilder().setProfileSelected(isProfileSelected).build()
        }
    }
}