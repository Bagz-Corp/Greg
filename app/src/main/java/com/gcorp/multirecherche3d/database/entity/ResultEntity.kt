package com.gcorp.multirecherche3d.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val parentId: Int,
    val sectionName: String,
    val title: String,
    val likeCount: Int,
    val imageUrl: String,
    val contentUrl: String
)
