package com.example.preteirb.data.usage

import com.example.preteirb.api.ItemApiService
import com.example.preteirb.api.UsageApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkUsagesRepository @Inject constructor(
    val usageApiService: UsageApiService,
    val itemApiService: ItemApiService
) : UsagesRepository {
    override fun getAllUsagesStream() =
        flow { emit(usageApiService.getUsages()) }

    override fun getAllUsagesByItemIdStream(itemId: Int) =
        flow { emit(itemApiService.getUsagesByItem(itemId)) }

    override fun getUsageStream(id: Int) = flow { emit(usageApiService.getUsage(id)) }

    override suspend fun insertUsage(usage: Usage) = usageApiService.createUsage(usage)

    override suspend fun insertUsageList(usages: List<Usage>) {
        usages.forEach() {
            usageApiService.createUsage(it)
        }
    }

    override suspend fun deleteUsage(usage: Usage) = usageApiService.deleteUsage(usage.id)

    override suspend fun updateUsage(usage: Usage) = usageApiService.updateUsage(usage.id, usage)

}