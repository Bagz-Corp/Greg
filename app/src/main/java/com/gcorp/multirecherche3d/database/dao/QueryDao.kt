package com.gcorp.multirecherche3d.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gcorp.multirecherche3d.database.entity.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao {

    @Query("SELECT * FROM queries WHERE search_query = :searchQuery LIMIT 1")
    fun findQuery(searchQuery: String): Flow<SearchQueryEntity?>

    @Upsert
    fun insertQuery(query: SearchQueryEntity)
}