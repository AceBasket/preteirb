package com.example.preteirb.data.usage

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Usage] from a given data source.
 */
interface UsagesRepository {
    /**
     * Retrieves all the usages from the given data source.
     */
    fun getAllUsagesStream(): Flow<List<Usage>>

    /**
     * Retrieves all the usages from the given data source that matches with the [itemId].
     */
    fun getAllUsagesByItemIdStream(itemId: Int): Flow<List<Usage>>

    /**
     * Retrieves an usage from the given data source that matches with the [id].
     */
    fun getUsageStream(id: Int): Flow<Usage?>

    /**
     * Insert usage in the data source
     */
    suspend fun insertUsage(usage: Usage)

    /**
     * Insert multiple usages at once in the data source
     */
    suspend fun insertUsageList(usages: List<Usage>)

    /**
     * Delete usage from the data source
     */
    suspend fun deleteUsage(usage: Usage)

    /**
     * Update usage from the data source
     */
    suspend fun updateUsage(usage: Usage)
}