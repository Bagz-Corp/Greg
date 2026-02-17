package com.gcorp.multirecherche3d.network.retrofit.service

import com.gcorp.multirecherche3d.network.model.MakerWorldPayload
import retrofit2.http.GET
import retrofit2.http.Query

interface MakerWorldService {

    @GET(value = "api/v1/search-service/select/design2")
    suspend fun search(
        @Query("keyword") keyword: String,
        @Query("orderBy") orderBy: String = "score",
        @Query("limit") limit: Int = 20
    ): MakerWorldPayload
}