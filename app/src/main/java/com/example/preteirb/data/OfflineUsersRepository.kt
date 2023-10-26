package com.example.preteirb.data

import kotlinx.coroutines.flow.Flow


class OfflineUsersRepository(private val userDao: UserDao) : UsersRepository {
    override fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers();
    
    override fun getUserStream(id: Int): Flow<User?> = userDao.getUser(id);
    
    override fun getItemsOwnedByUserStream(id: Int): Flow<ItemsOwned?> =
        userDao.getAllItemsOwnedByUser(id);
    
    override suspend fun insertUser(user: User) = userDao.insert(user);
    
    override suspend fun deleteUser(user: User) = userDao.delete(user);
    
    override suspend fun updateUser(user: User) = userDao.update(user);
}