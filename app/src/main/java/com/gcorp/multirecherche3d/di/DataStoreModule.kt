package com.gcorp.multirecherche3d.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.gcorp.shared.DATA_STORE_FILE_NAME
import com.gcorp.shared.PreferencesDataSource
import com.gcorp.shared.createDataStore
import com.gcorp.shared.model.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okio.FileSystem
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<UserPreferences> {
        return createDataStore(
            fileSystem = FileSystem.SYSTEM,
            producePath = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
        )
    }

    @Provides
    @Singleton
    fun providePreferencesDataSource(dataStore: DataStore<UserPreferences>): PreferencesDataSource {
        return PreferencesDataSource(dataStore)
    }
}
