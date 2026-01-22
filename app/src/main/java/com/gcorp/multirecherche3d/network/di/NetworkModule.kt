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
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("NetworkClient") {
        OkHttpClient.Builder().build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface RetrofitModule {
    @Binds
    fun binds(impl: RetrofitDataSource): RemoteDataSource
}