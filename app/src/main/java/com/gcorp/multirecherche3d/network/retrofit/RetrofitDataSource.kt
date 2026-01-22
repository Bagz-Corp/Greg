package com.gcorp.multirecherche3d.network.retrofit

import androidx.compose.ui.util.trace
import com.gcorp.multirecherche3d.network.RemoteDataSource
import com.gcorp.multirecherche3d.network.model.SketchFabModel
import dagger.Lazy
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitDataSource @Inject constructor(
    okhttpCallFactory: Lazy<Call.Factory>,
): RemoteDataSource {

    private val apiSource : RetrofitApi = trace("Retrofit") {
        Retrofit
            .Builder()
            .baseUrl("https://sketchfab.com/i/")
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(LogInterceptor())
                    .build()
            )
            .build()
            .create(RetrofitApi::class.java)
    }

    override suspend fun fetchSketchFab(searchQuery: String): List<SketchFabModel> =
        apiSource.fetchSketchFab(searchQuery).results
}

private class LogInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        println("Sending request ${request.url} on ${chain.connection()}${request.headers}")

        val response: Response = chain.proceed(request)
        println("Received response ${response.code} -> ${response.body}")

        return response
    }
}
