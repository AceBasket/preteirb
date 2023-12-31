package com.example.preteirb.data.item

import com.example.preteirb.model.items_owned.ItemDetails
import kotlinx.coroutines.flow.Flow

/**
 *  Repository that provides insert, update, delete, and retrieve of [ItemDto] from a given data source.
 */
interface ItemsRepository {
    /**
     * Retrieves all the items from the given data source.
     */
    suspend fun getAllItemsStream(): Flow<List<ItemDto>>

    /**
     * Retrieves an item from the given data source that matches with the [id].
     */
    suspend fun getItemStream(id: Int): Flow<ItemDto?>

    /**
     * Retrieves an item and all the usages associated with it.
     */
    suspend fun getItemAndUsagesStream(id: Int): Flow<ItemAndUsagesDto?>

    /**
     * Retrieves all the items from the given data source that matches with the [query].
     */
    suspend fun getItemsFromQueryStream(query: String): Flow<List<ItemDto>>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: ItemDetails): ItemDto

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(itemDto: ItemDto)

    /**
     * Update item from the data source
     */
    suspend fun updateItem(item: ItemDetails): ItemDto
    suspend fun updateItemName(item: ItemDetails): ItemDto
}