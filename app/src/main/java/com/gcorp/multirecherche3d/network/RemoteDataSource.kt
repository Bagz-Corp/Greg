package com.gcorp.multirecherche3d.network

import com.gcorp.multirecherche3d.network.model.MakerWorldModel
import com.gcorp.multirecherche3d.network.model.SketchFabModel

interface RemoteDataSource {

    suspend fun fetchSketchFab(searchQuery: String): List<SketchFabModel>

    suspend fun fetchMakerWorld(keyword: String): List<MakerWorldModel>

}