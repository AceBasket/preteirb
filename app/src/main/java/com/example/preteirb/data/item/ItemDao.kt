package com.example.preteirb.data.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item): Long;
    
    @Update
    suspend fun update(item: Item);
    
    @Delete
    suspend fun delete(item: Item);
    
    @Query("SELECT * FROM items WHERE itemId = :id")
    fun getItem(id: Int): Flow<Item>;
    
    @Query(
        "SELECT * FROM items " +
                "WHERE name " +
                "LIKE '%' || :query || '%' " +
                "ORDER BY (" +
                "CASE WHEN name = :query THEN 1 " +
                "WHEN name LIKE :query || '%' THEN 2 " +
                "ELSE 3 END)"
    )
    fun getItemsFromQuery(query: String): Flow<List<Item>>;
    
    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>;
    
    @Transaction
    @Query("SELECT * FROM items WHERE itemId = :itemId")
    fun getItemAndUsages(itemId: Int): Flow<ItemAndUsages>;
}