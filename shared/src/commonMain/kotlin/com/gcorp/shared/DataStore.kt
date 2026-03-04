package com.gcorp.shared

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.gcorp.shared.model.UserPreferences
import com.gcorp.shared.model.UserPreferencesSerializer
import okio.FileSystem
import okio.Path.Companion.toPath

fun createDataStore(fileSystem: FileSystem, producePath: () -> String): DataStore<UserPreferences> =
    DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = fileSystem,
            serializer = UserPreferencesSerializer,
            producePath = { producePath().toPath() }
        )
    )

const val DATA_STORE_FILE_NAME = "user_prefs.json"
