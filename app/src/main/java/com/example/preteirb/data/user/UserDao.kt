package com.example.preteirb.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.preteirb.data.item.ItemsOwned
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long;
    
    @Delete
    suspend fun delete(user: User);
    
    @Update
    suspend fun update(user: User);
    
    @Query("SELECT * from users WHERE userId = :id")
    fun getUser(id: Int): Flow<User>;
    
    @Query("SELECT * from users ORDER BY username ASC")
    fun getAllUsers(): Flow<List<User>>;
    
    @Transaction
    @Query("SELECT * from users WHERE userId = :id")
    fun getAllItemsOwnedByUser(id: Int): Flow<ItemsOwned>;
}