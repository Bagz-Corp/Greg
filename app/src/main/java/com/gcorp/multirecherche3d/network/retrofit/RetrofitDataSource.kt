package com.gcorp.multirecherche3d.network.retrofit

import androidx.compose.ui.util.trace
import com.gcorp.multirecherche3d.network.RemoteDataSource
import dagger.Lazy
import okhttp3.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitDataSource @Inject constructor(
    okhttpCallFactory: Lazy<Call.Factory>,
): RemoteDataSource {

    private val apiSource : RetrofitApi = trace("Retrofit") {
        Retrofit
            .Builder()
            .baseUrl("https://makerworld.com/_next/data/uPhbnYUKdOCCcMs_Q_C7s/en/search/models.json/")
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .build()
            .create(RetrofitApi::class.java)
    }

    override suspend fun fetchMakersWorld(searchQuery: String): List<String> =
        apiSource.fetchMakersWorld(searchQuery)
}
