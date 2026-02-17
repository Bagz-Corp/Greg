package com.gcorp.multirecherche3d.network.retrofit.service

import com.gcorp.multirecherche3d.network.model.SketchFabPayload
import retrofit2.http.GET
import retrofit2.http.Query

interface SketchFabService {

    @GET(value = "search")
    suspend fun search(
        @Query("q") search: String,
        @Query("sort_by") sort: String = "-relevance",
        @Query("type") type: String = "models"
    ): SketchFabPayload

}