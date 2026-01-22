package com.gcorp.multirecherche3d.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SketchFabPayload(
    //@SerializedName("results")
    val results: List<SketchFabModel>,

    val next: String
)

@Serializable
data class SketchFabModel(
    //@SerializedName("name")
    val name: String,
    //@SerializedName("embedUrl")
    val embedUrl: String,
)