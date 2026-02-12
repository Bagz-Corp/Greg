package com.gcorp.multirecherche3d.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queries")
data class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "search_query") val searchQuery: String,
    val results: List<ResultEntity>
)

data class ResultEntity(
    val title: String,
    val likeCount: Int,
    val thumbnail: String,
    val url: String
)

