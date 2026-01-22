package com.gcorp.multirecherche3d.network

interface RemoteDataSource {

    suspend fun fetchMakersWorld(searchQuery: String): List<String>

}