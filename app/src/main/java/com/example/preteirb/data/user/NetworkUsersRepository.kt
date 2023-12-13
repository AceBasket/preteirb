package com.example.preteirb.data.user

import com.example.preteirb.api.ProfileApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkUsersRepository @Inject constructor(val profileApiService: ProfileApiService) :
    UsersRepository {
    override suspend fun getAllUsersStream() = flow { emit(profileApiService.getProfiles()) }

    override suspend fun getUserStream(id: Int) = flow { emit(profileApiService.getProfile(id)) }

    override suspend fun getAllItemsOwnedByUserStream(id: Int) =
        flow { emit(profileApiService.getItemsOwnedByUser(id)) }

    override suspend fun getAllItemsBookedAndNotOwnedByUserStream(id: Int) =
        flow { emit(profileApiService.getUsagesAndItemWithOwnerByUser(id)) }

    override suspend fun insertUser(user: User) = profileApiService.createProfile(user).id.toLong()

    override suspend fun deleteUser(user: User) = profileApiService.deleteProfile(user.id)

    override suspend fun updateUser(user: User) = profileApiService.updateProfile(user.id, user)
}