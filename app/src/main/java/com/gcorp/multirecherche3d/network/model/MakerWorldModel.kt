package com.gcorp.multirecherche3d.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MakerWorldPayload(
    val total: Int,
    @SerializedName("hits") val results: List<MakerWorldModel>
)

data class MakerWorldModel(
    val id: Int,
    val title: String,
    val likeCount: Int,
    @SerializedName("createTime") val publishedAt: String,
    @SerializedName("cover") val imageUrl: String
) {
    /** Generate the url to access the model, this data is not sent by the MakerWorld API */
    fun generateContentUrl(): String = "https://makerworld.com/en/models/$id"
}