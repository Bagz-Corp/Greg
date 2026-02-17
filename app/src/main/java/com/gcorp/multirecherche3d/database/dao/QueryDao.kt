package com.gcorp.multirecherche3d.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gcorp.multirecherche3d.database.entity.SearchResultsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao {

    @Query("SELECT * FROM searchResults WHERE search_query = :searchQuery LIMIT 1")
    fun getResults(searchQuery: String): Flow<SearchResultsEntity?>

    @Upsert
    fun insertResults(query: SearchResultsEntity)

}