package com.gcorp.multirecherche3d.network

import com.gcorp.multirecherche3d.network.model.SketchFabModel

interface RemoteDataSource {

    suspend fun fetchSketchFab(searchQuery: String): List<SketchFabModel>

}