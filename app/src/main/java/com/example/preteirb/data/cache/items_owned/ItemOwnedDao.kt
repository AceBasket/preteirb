package com.example.preteirb.data.cache.items_owned

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemOwnedDao {
    @Insert
    suspend fun insert(itemOwned: ItemOwned)

    @Insert
    suspend fun insertAll(itemOwnedList: List<ItemOwned>)

    @Update
    suspend fun update(itemOwned: ItemOwned)

    @Delete
    suspend fun delete(itemOwned: ItemOwned)

    @Delete
    suspend fun deleteAll(itemOwnedList: List<ItemOwned>)

    @Query("DELETE FROM items_owned")
    suspend fun deleteAll()

    @Query("SELECT * FROM items_owned")
    suspend fun getAll(): List<ItemOwned>
}