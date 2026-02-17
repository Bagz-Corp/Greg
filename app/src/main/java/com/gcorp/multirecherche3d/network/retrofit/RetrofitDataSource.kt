package com.gcorp.multirecherche3d.network.retrofit

import androidx.compose.ui.util.trace
import com.gcorp.multirecherche3d.network.RemoteDataSource
import com.gcorp.multirecherche3d.network.model.MakerWorldModel
import com.gcorp.multirecherche3d.network.model.SketchFabModel
import com.gcorp.multirecherche3d.network.retrofit.service.MakerWorldService
import com.gcorp.multirecherche3d.network.retrofit.service.SketchFabService
import dagger.Lazy
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitDataSource @Inject constructor(
    okhttpCallFactory: Lazy<Call.Factory>,
): RemoteDataSource {

    private val sketchFabApiSource : SketchFabService = trace("sketchFabApiSource") {
        Retrofit
            .Builder()
            .baseUrl("https://sketchfab.com/i/")
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SketchFabService::class.java)
    }

    private val makerWorldApiSource: MakerWorldService = trace("makerWorldApiSource") {
        Retrofit
            .Builder()
            .baseUrl("https://makerworld.com/")
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MakerWorldService::class.java)
    }

    override suspend fun fetchSketchFab(searchQuery: String): List<SketchFabModel> =
        sketchFabApiSource.search(searchQuery).results

    override suspend fun fetchMakerWorld(keyword: String): List<MakerWorldModel> =
        makerWorldApiSource.search(keyword).results
}


