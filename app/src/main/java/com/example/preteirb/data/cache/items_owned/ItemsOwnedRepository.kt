package com.example.preteirb.data.cache.items_owned

import javax.inject.Inject

class ItemsOwnedRepository @Inject constructor(private val itemOwnedDao: ItemOwnedDao) {
    suspend fun insert(itemOwned: ItemOwned) {
        itemOwnedDao.insert(itemOwned)
    }

    suspend fun insertAll(itemOwnedList: List<ItemOwned>) {
        itemOwnedDao.insertAll(itemOwnedList)
    }

    suspend fun update(itemOwned: ItemOwned) {
        itemOwnedDao.update(itemOwned)
    }

    suspend fun getAll(): List<ItemOwned> {
        return itemOwnedDao.getAll()
    }

    suspend fun delete(itemOwned: ItemOwned) {
        itemOwnedDao.delete(itemOwned)
    }

    suspend fun deleteAll(itemOwnedList: List<ItemOwned>) {
        itemOwnedDao.deleteAll(itemOwnedList)
    }

    suspend fun deleteAll() {
        itemOwnedDao.deleteAll()
    }
}