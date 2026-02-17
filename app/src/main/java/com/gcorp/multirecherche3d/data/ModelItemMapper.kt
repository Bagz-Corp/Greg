package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.database.entity.ResultEntity
import com.gcorp.multirecherche3d.database.entity.SearchResultsEntity
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.Thumbnail
import com.gcorp.multirecherche3d.domain.model.getBigger
import com.gcorp.multirecherche3d.network.model.MakerWorldModel
import com.gcorp.multirecherche3d.network.model.SketchFabModel

fun SketchFabModel.toModelItem() = ModelItem(
    sectionName = ModelType.SKETCH_FAB.value,
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

fun MakerWorldModel.toModelItem() = ModelItem(
    sectionName = ModelType.MAKER_WORLD.value,
    thumbnails = listOf(
        Thumbnail(
            url = imageUrl
        )
    ),
    title = title,
    likeCount = likeCount,
    url = generateContentUrl(),
)

fun SearchResultsEntity.asModelItems(): List<ModelItem> = buildList {
    addAll(sketchFabResults.toModelItems(ModelType.SKETCH_FAB))
    addAll(makerWorldResults.toModelItems(ModelType.MAKER_WORLD))
}

// Extension function for better readability
private fun List<ResultEntity>.toModelItems(type: ModelType) = map {
    ModelItem(
        sectionName = type.value,
        thumbnails = listOf(Thumbnail(url = it.imageUrl)),
        title = it.title,
        likeCount = it.likeCount,
        url = it.contentUrl
    )
}

fun List<ModelItem>.asResultEntities(): List<ResultEntity> =
    this.map {
        ResultEntity(
            title = it.title,
            likeCount = it.likeCount,
            imageUrl = it.thumbnails.getBigger().url,
            contentUrl = it.url
        )
    }

enum class ModelType(val value: String) {
    SKETCH_FAB("sketchFab"),
    MAKER_WORLD("makerWorld")
}