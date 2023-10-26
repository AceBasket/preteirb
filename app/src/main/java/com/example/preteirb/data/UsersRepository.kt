package com.example.preteirb.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [User] from a given data source.
 */
interface UsersRepository {
    /**
     * Retrieve all the users from the the given data source.
     */
    fun getAllUsersStream(): Flow<List<User>>
    
    /**
     * Retrieve a user from the given data source that matches with the [id].
     */
    fun getUserStream(id: Int): Flow<User?>
    
    /**
     * Retrieves all the items owned by a user from the given data source that matches with the [id].
     */
    fun getItemsOwnedByUserStream(id: Int): Flow<ItemsOwned?>
    
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