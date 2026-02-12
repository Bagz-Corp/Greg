package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.database.dao.QueryDao
import com.gcorp.multirecherche3d.database.entity.SearchQueryEntity
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.network.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import javax.inject.Inject

class SearchRepository @Inject constructor(
    val network: RemoteDataSource,
    val searchQueryDao: QueryDao
) {
    private val _query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<List<ModelItem>?> = _query
        .debounce(300L) // Wait for 300ms pause in typing
        .distinctUntilChanged() // Only proceed if query actually changed
        .flatMapLatest { query ->
            searchQueryDao
                .findQuery(query)
                .mapLatest { it?.asModelItems() }
        }

    suspend fun updateQuery(query: String) {
        _query.value = query

        val results = network.fetchSketchFab(query).map { it.toModelItem() }

        withContext(Dispatchers.IO) {
            sleep(3000L)
            searchQueryDao.insertQuery(
                query = SearchQueryEntity(
                    searchQuery = query,
                    results = results.asResultEntities()
                )
            )
        }
    }

    suspend fun multiSearch(searchQuery: String): List<ModelItem> {
        return network.fetchSketchFab(searchQuery).map { it.toModelItem() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSearchResults(searchQuery: String): Flow<List<ModelItem>?> =
        searchQueryDao
            .takeIf { searchQuery.isNotEmpty() }
            ?.findQuery(searchQuery)
            ?.mapLatest { it?.asModelItems() }
            ?: emptyFlow()

}