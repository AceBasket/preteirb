package com.example.preteirb.data.usage

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineUsagesRepository @Inject constructor(private val usageDao: UsageDao) :
    UsagesRepository {
    override fun getAllUsagesStream(): Flow<List<Usage>> = usageDao.getAllUsages();
    
    override fun getUsageStream(id: Int): Flow<Usage?> = usageDao.getUsage(id);
    
    override suspend fun insertUsage(usage: Usage) = usageDao.insert(usage);
    
    override suspend fun insertUsageList(usages: List<Usage>) = usageDao.insert(usages);
    
    override suspend fun deleteUsage(usage: Usage) = usageDao.delete(usage);
    
    override suspend fun updateUsage(usage: Usage) = usageDao.update(usage);
}