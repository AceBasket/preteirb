package com.example.preteirb.data.item

import com.example.preteirb.api.ItemApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkItemsRepository @Inject constructor(val itemApiService: ItemApiService) :
    ItemsRepository {
    override suspend fun getAllItemsStream() = flow { emit(itemApiService.getItems()) }

    override suspend fun getItemStream(id: Int) = flow { emit(itemApiService.getItem(id)) }
    override suspend fun getItemAndUsagesStream(id: Int) =
        flow { emit(itemApiService.getUsagesByItem(id)) }

    override suspend fun getItemsFromQueryStream(query: String) =
        flow { emit(itemApiService.searchItems(query)) }


    override suspend fun insertItem(item: Item) = itemApiService.createItem(item).id.toLong()

    override suspend fun deleteItem(item: Item) = itemApiService.deleteItem(item.id)

    override suspend fun updateItem(item: Item) = itemApiService.updateItem(item.id, item)
}