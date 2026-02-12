package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.database.entity.ResultEntity
import com.gcorp.multirecherche3d.database.entity.SearchQueryEntity
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.Thumbnail
import com.gcorp.multirecherche3d.domain.model.getBigger
import com.gcorp.multirecherche3d.network.model.SketchFabModel

fun SketchFabModel.toModelItem() = ModelItem(
    thumbnails = thumbnails.images.map {
        Thumbnail(
            url = it.url,
            width = it.width,
            height = it.height
        )
    },
    title = this.name,
    likeCount = this.likeCount,
    url = viewerUrl
)

fun SearchQueryEntity.asModelItems(): List<ModelItem> =
    this.results.map {
        ModelItem(
            thumbnails = listOf(
                Thumbnail(
                    url = it.thumbnail
                )
            ),
            title = it.title,
            likeCount = it.likeCount,
            url = it.url
        )
    }

fun List<ModelItem>.asResultEntities(): List<ResultEntity> =
    this.map {
        ResultEntity(
            title = it.title,
            likeCount = it.likeCount,
            thumbnail = it.thumbnails.getBigger().url,
            url = it.url
        )
    }
