package com.gcorp.multirecherche3d.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET(value = "makersWorld")
    suspend fun fetchMakersWorld(
        @Query("keyword") search: String
    ): List<String>

}