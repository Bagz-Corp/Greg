package com.gcorp.shared

import androidx.datastore.core.DataStore
import com.gcorp.shared.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataSource(
    private val dataStore: DataStore<UserPreferences>
) {
    val favoriteIds: Flow<List<Int>> = dataStore.data.map { it.favoriteIds }

    suspend fun addFavorite(id: Int) {
        dataStore.updateData { current ->
            if (current.favoriteIds.contains(id)) current
            else current.copy(favoriteIds = current.favoriteIds + id)
        }
    }

    suspend fun removeFavorite(id: Int) {
        dataStore.updateData { current ->
            current.copy(favoriteIds = current.favoriteIds - id)
        }
    }
}
