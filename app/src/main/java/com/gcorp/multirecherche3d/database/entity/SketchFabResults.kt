package com.gcorp.multirecherche3d.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchResults")
data class SearchResultsEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "search_query") val searchQuery: String,
    val sketchFabResults: List<ResultEntity>,
    val makerWorldResults: List<ResultEntity>
)
