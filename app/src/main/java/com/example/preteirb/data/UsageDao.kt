package com.example.preteirb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usage: Usage);
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usages: List<Usage>);
    
    @Delete
    suspend fun delete(usage: Usage);
    
    @Update
    suspend fun update(usage: Usage);
    
    @Query("SELECT * from usages WHERE userId = :userId AND itemId = :itemId")
    fun getUsage(userId: Int, itemId: Int): Flow<Usage>;
    
    @Query("SELECT * from usages")
    fun getAllUsages(): Flow<List<Usage>>;
}