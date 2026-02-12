package com.gcorp.multirecherche3d.domain

import com.gcorp.multirecherche3d.data.SearchRepository
import com.gcorp.multirecherche3d.domain.model.ModelItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSearchResultsUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {

//    suspend operator fun invoke(searchQuery: String): List<ModelItem> {
//        return searchRepository.multiSearch(searchQuery)
//    }

    fun getResults(): Flow<List<ModelItem>?> = searchRepository.searchResults

    suspend fun updateQuery(query: String) = searchRepository.updateQuery(query)

}
