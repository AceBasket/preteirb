package com.example.preteirb.data.user

import com.example.preteirb.data.item.ItemsOwned
import com.example.preteirb.data.usage.UsageWithItemAndUser
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [User] from a given data source.
 */
interface UsersRepository {
    /**
     * Retrieve all the users from the the given data source.
     */
    suspend fun getAllUsersStream(): Flow<List<User>>

    /**
     * Retrieve a user from the given data source that matches with the [id].
     */
    suspend fun getUserStream(id: Int): Flow<User?>

    /**
     * Retrieves all the items owned by a user from the given data source that matches with the [id].
     */
    suspend fun getAllItemsOwnedByUserStream(id: Int): Flow<ItemsOwned?>

    /**
     * Retrieves all the usages of items not owned by the user from the given data source that matches with the [id].
     */
    suspend fun getAllItemsBookedAndNotOwnedByUserStream(id: Int): Flow<List<UsageWithItemAndUser>>

    /**
     * Insert user in the data source
     */
    suspend fun insertUser(user: User): Long

    /**
     * Delete user from the data source
     */
    suspend fun deleteUser(user: User)

    /**
     * Update user in the data source
     */
    suspend fun updateUser(user: User)


}