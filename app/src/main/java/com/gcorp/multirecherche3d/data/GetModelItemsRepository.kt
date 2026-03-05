package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.database.dao.QueryDao
import com.gcorp.multirecherche3d.domain.model.ModelItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetModelItemsRepository @Inject constructor(
    val searchQueryDao: QueryDao
) {

    suspend fun getFavorites(ids: List<Int>): List<ModelItem> = withContext(Dispatchers.IO) {
        searchQueryDao.getResultsByIds(ids).map { it.asModelItem(isFavorite = true) }
    }
}