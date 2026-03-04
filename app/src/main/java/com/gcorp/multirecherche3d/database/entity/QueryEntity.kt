package com.gcorp.multirecherche3d.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "query_table")
data class QueryEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "search_query") val searchQuery: String
)

data class QueryWithResults(
    @Embedded val query: QueryEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "parentId"
    ) val results: List<ResultEntity>,
)