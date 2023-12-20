package com.example.preteirb.data.item

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.preteirb.api.ItemApiService
import com.example.preteirb.model.items_owned.ItemDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class NetworkItemsRepository @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val itemApiService: ItemApiService
) :
    ItemsRepository {
    override suspend fun getAllItemsStream() = flow { emit(itemApiService.getItems()) }

    override suspend fun getItemStream(id: Int) = flow { emit(itemApiService.getItem(id)) }
    override suspend fun getItemAndUsagesStream(id: Int) =
        flow { emit(itemApiService.getUsagesByItem(id)) }

    override suspend fun getItemsFromQueryStream(query: String) =
        flow { emit(itemApiService.searchItems(query)) }

    override suspend fun deleteItem(item: Item) = itemApiService.deleteItem(item.id)

    override suspend fun insertItem(item: ItemDetails) =
        updateOrCreateItem(item, true)

    override suspend fun updateItem(item: ItemDetails) =
        updateOrCreateItem(item, false)

    private suspend fun updateOrCreateItem(item: ItemDetails, isCreate: Boolean): Item {
        if (item.image == Uri.EMPTY) {
            val updatedItem = Item(
                id = item.id,
                name = item.name,
                description = item.description,
                image = null,
                ownerId = item.ownerId
            )
            return if (isCreate) {
                itemApiService.createItemWithoutImage(updatedItem)
            } else {
                itemApiService.updateItemWithoutImage(updatedItem.id, updatedItem)
            }
        }
        val imagePart = uriToMultipartBody(item.image)
        return if (isCreate) {
            itemApiService.createItem(
                name = item.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                description = item.description.toRequestBody("text/plain".toMediaTypeOrNull()),
                image = imagePart,
                owner = item.ownerId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            )
        } else {
            itemApiService.updateItem(
                id = item.id,
                name = item.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                description = item.description.toRequestBody("text/plain".toMediaTypeOrNull()),
                image = imagePart,
                owner = item.ownerId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    private fun uriToMultipartBody(uri: Uri): MultipartBody.Part {
        // Use ContentResolver to obtain the real file path from the Uri
        val contentResolver = applicationContext.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        val filePath: String? = if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(columnIndex)
            cursor.close()
            path
        } else {
            uri.path
        }

        // Create RequestBody from file
        val file = File(filePath!!)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        // Create MultipartBody.Part from RequestBody
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}