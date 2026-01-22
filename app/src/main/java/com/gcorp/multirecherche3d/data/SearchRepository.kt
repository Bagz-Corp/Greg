package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.network.RemoteDataSource
import javax.inject.Inject

class SearchRepository @Inject constructor(
    val dataSource: RemoteDataSource
) {

    suspend fun multiSearch(searchQuery: String): List<String> {
        return dataSource.fetchMakersWorld(searchQuery)
    }

}