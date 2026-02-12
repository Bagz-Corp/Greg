package com.gcorp.multirecherche3d.database.di

import android.content.Context
import androidx.room.Room
import com.gcorp.multirecherche3d.database.AppDatabase
import com.gcorp.multirecherche3d.database.dao.QueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "Greg-database"
    ).build()

    @Provides
    fun providesQueryDao(
        database: AppDatabase
    ): QueryDao = database.queryDao()

}