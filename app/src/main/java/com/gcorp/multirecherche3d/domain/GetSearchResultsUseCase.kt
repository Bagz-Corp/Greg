package com.gcorp.multirecherche3d.domain

import com.gcorp.multirecherche3d.data.SearchRepository
import com.gcorp.multirecherche3d.domain.model.ModelItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSearchResultsUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {

    fun getResults(): Flow<List<ModelItem>?> = searchRepository.results

    suspend fun updateQuery(query: String) = searchRepository.updateQuery(query)

}
