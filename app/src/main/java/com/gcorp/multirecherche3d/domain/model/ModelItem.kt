package com.gcorp.multirecherche3d.domain.model

data class ModelItem(
    val sectionName: String,
    val thumbnails: List<Thumbnail>,
    val title: String,
    val likeCount: Int,
    val url: String
)

data class Thumbnail(
    val url: String,
    val width: Int = 0,
    val height: Int = 0
)

fun List<Thumbnail>.getBigger() = maxBy { it.width }

enum class ModelType(val value: String) {
    SKETCH_FAB("sketchFab"),
    MAKER_WORLD("makerWorld")
}