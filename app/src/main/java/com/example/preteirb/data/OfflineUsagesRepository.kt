package com.example.preteirb.data

import kotlinx.coroutines.flow.Flow

class OfflineUsagesRepository(private val usageDao: UsageDao) : UsagesRepository {
    override fun getAllUsagesStream(): Flow<List<Usage>> = usageDao.getAllUsages();
    
    override fun getUsageStream(itemId: Int, userId: Int): Flow<Usage?> =
        usageDao.getUsage(itemId, userId);
    
    override suspend fun insertUsage(item: Usage) = usageDao.insert(item);
    
    override suspend fun insertUsageList(items: List<Usage>) = usageDao.insert(items);
    
    override suspend fun deleteUsage(item: Usage) = usageDao.delete(item);
    
    override suspend fun updateUsage(item: Usage) = usageDao.update(item);
}