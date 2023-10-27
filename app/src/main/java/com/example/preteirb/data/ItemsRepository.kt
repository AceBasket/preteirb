package com.example.preteirb.data

import kotlinx.coroutines.flow.Flow

/**
 *  Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface ItemsRepository {
    /**
     * Retrieves all the items from the given data source.
     */
    fun getAllItemsStream(): Flow<List<Item>>
    
    /**
     * Retrieves an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<Item?>
    
    /**
     * Retrieves an item and all the usages associated with it.
     */
    fun getItemAndUsagesStream(id: Int): Flow<ItemAndUsages?>
    
    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Item): Long
    
    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: Item)
    
    /**
     * Update item from the data source
     */
    suspend fun updateItem(item: Item)
}