package com.example.preteirb.data.item

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems();
    
    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id);
    
    override fun getItemAndUsagesStream(id: Int): Flow<ItemAndUsages?> =
        itemDao.getItemAndUsages(id);
    
    override fun getItemsFromQueryStream(query: String): Flow<List<Item>> =
        itemDao.getItemsFromQuery(query);
    
    override suspend fun insertItem(item: Item) = itemDao.insert(item);
    
    override suspend fun deleteItem(item: Item) = itemDao.delete(item);
    
    override suspend fun updateItem(item: Item) = itemDao.update(item);
}