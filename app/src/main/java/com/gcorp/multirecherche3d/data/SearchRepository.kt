package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.database.dao.QueryDao
import com.gcorp.multirecherche3d.database.entity.SearchResultsEntity
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.network.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    val network: RemoteDataSource,
    val searchQueryDao: QueryDao
) {
    private val _query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val results: Flow<List<ModelItem>?> = _query
        .flatMapLatest { query ->
            searchQueryDao
                .getResults(query)
                .mapLatest {
                    it?.asModelItems()
                }
        }

    suspend fun updateQuery(query: String) {
        _query.value = query

        try {
            val sketchFabResults = network.fetchSketchFab(query).map { it.toModelItem() }
            val makerWorldResults = network.fetchMakerWorld(query).map { it.toModelItem() }

            withContext(Dispatchers.IO) {
                searchQueryDao.insertResults(
                    query = SearchResultsEntity(
                        searchQuery = query,
                        sketchFabResults = sketchFabResults.asResultEntities(),
                        makerWorldResults = makerWorldResults.asResultEntities()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error (e.g., notify UI or log)
        }
    }
}