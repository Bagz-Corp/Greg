package com.gcorp.multirecherche3d.network.retrofit

import com.gcorp.multirecherche3d.network.model.SketchFabPayload
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET(value = "search")
    suspend fun fetchSketchFab(
        @Query("q") search: String,
        @Query("sort_by") sort: String = "-relevance",
        @Query("type") type: String = "models"
    ): SketchFabPayload

}