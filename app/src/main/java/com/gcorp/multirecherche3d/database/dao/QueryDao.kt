package com.gcorp.multirecherche3d.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gcorp.multirecherche3d.database.entity.QueryEntity
import com.gcorp.multirecherche3d.database.entity.QueryWithResults
import com.gcorp.multirecherche3d.database.entity.ResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao {

    @Transaction
    @Query("SELECT * FROM query_table WHERE search_query = :searchQuery LIMIT 1")
    fun getQueryResults(searchQuery: String): Flow<QueryWithResults?>

    @Upsert
    fun insertQuery(query: QueryEntity)

    @Query("SELECT * FROM query_table WHERE search_query = :searchQuery LIMIT 1")
    fun getQuery(searchQuery: String): QueryEntity

    @Upsert
    fun insertResults(results: List<ResultEntity>)

    @Query("SELECT * FROM results WHERE id IN (:ids)")
    fun getResultsByIds(ids: List<Int>): List<ResultEntity>
}