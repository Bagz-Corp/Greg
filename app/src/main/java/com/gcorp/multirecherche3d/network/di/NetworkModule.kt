package com.gcorp.multirecherche3d.network.di

import androidx.compose.ui.util.trace
import com.gcorp.multirecherche3d.network.RemoteDataSource
import com.gcorp.multirecherche3d.network.retrofit.RetrofitDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("NetworkClient") {
        OkHttpClient
            .Builder()
            .addInterceptor(UserAgentInterceptor())
            .addInterceptor(LogInterceptor())
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface RetrofitModule {
    @Binds
    fun binds(impl: RetrofitDataSource): RemoteDataSource
}

private class UserAgentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
            .build()
        return chain.proceed(request)
    }
}

/**
 * Careful in case of using response.body.string() as the size might exceeds the tolerated buffer
 */
private class LogInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        println("Sending request ${request.url}")

        val response: Response = chain.proceed(request)
        println("Received response ${response.code}")

        return response
    }
}