package com.example.preteirb.data.user

import com.example.preteirb.data.item.ItemDto
import com.example.preteirb.data.usage.UsageWithItemAndUser
import com.example.preteirb.model.ProfileDetails
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [UserDto] from a given data source.
 */
interface UsersRepository {
    /**
     * Retrieve all the users from the the given data source.
     */
    suspend fun getAllUsersStream(): Flow<List<UserDto>>

    /**
     * Retrieve a user from the given data source that matches with the [id].
     */
    suspend fun getUserStream(id: Int): Flow<UserDto?>

    /**
     * Retrieves all the items owned by a user from the given data source that matches with the [id].
     */
    suspend fun getAllItemsOwnedByUserStream(id: Int): Flow<List<ItemDto>>

    /**
     * Retrieves all the usages of items not owned by the user from the given data source that matches with the [id].
     */
    suspend fun getAllItemsBookedAndNotOwnedByUserStream(id: Int): Flow<List<UsageWithItemAndUser>>

    /**
     * Insert user in the data source
     */
    suspend fun insertUser(user: ProfileDetails): UserDto

    /**
     * Delete user from the data source
     */
    suspend fun deleteUser(userDto: UserDto)

    /**
     * Update user in the data source
     */
    suspend fun updateUser(user: ProfileDetails): UserDto


    suspend fun updateUsername(user: ProfileDetails): UserDto
}