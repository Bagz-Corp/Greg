package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.database.entity.QueryWithResults
import com.gcorp.multirecherche3d.database.entity.ResultEntity
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.Thumbnail
import com.gcorp.multirecherche3d.network.model.MakerWorldModel
import com.gcorp.multirecherche3d.network.model.SketchFabModel

fun SketchFabModel.toResultEntity(parentId: Int) = ResultEntity(
    parentId = parentId,
    sectionName = ModelType.SKETCH_FAB.value,
    title = this.name,
    likeCount = this.likeCount,
    imageUrl = this.thumbnails.images.first().url,
    contentUrl = viewerUrl,
)

fun MakerWorldModel.toResultEntity(parentId: Int) = ResultEntity(
    parentId = parentId,
    sectionName = ModelType.MAKER_WORLD.value,
    title = title,
    likeCount = likeCount,
    imageUrl = imageUrl,
    contentUrl = generateContentUrl(),
)

fun ResultEntity.asModelItem(
    isFavorite: Boolean = false
): ModelItem =
    ModelItem(
        id = id,
        sectionName = sectionName,
        thumbnails = listOf(
            Thumbnail(
                url = imageUrl,
                width = 1,
                height = 1
            )
        ),
        title = title,
        likeCount = likeCount,
        url = contentUrl,
        isFavorite = isFavorite
    )

fun QueryWithResults.asModelItems(): List<ModelItem> =
        this@asModelItems.results.map { it.asModelItem() }

enum class ModelType(val value: String) {
    SKETCH_FAB("sketchFab"),
    MAKER_WORLD("makerWorld")
}