package com.gcorp.multirecherche3d.data

import com.gcorp.multirecherche3d.database.entity.ResultEntity
import com.gcorp.multirecherche3d.network.model.ImageData
import com.gcorp.multirecherche3d.network.model.MakerWorldModel
import com.gcorp.multirecherche3d.network.model.SketchFabModel
import com.gcorp.multirecherche3d.network.model.Thumbnails
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelItemMapperTest {

    @Test
    fun `SketchFabModel toResultEntity maps correctly`() {
        val sketchFabModel = SketchFabModel(
            name = "Test Model",
            viewerUrl = "https://sketchfab.com/test",
            publishedAt = "2023-01-01",
            likeCount = 100,
            thumbnails = Thumbnails(
                images = arrayListOf(ImageData("https://thumb.url", 200, 200))
            )
        )

        val result = sketchFabModel.toResultEntity(parentId = 10)

        assertEquals(10, result.parentId)
        assertEquals(ModelType.SKETCH_FAB.value, result.sectionName)
        assertEquals("Test Model", result.title)
        assertEquals(100, result.likeCount)
        assertEquals("https://thumb.url", result.imageUrl)
        assertEquals("https://sketchfab.com/test", result.contentUrl)
    }

    @Test
    fun `MakerWorldModel toResultEntity maps correctly`() {
        val makerWorldModel = MakerWorldModel(
            id = 123,
            title = "Maker Model",
            likeCount = 50,
            publishedAt = "2023-02-02",
            imageUrl = "https://maker.url"
        )

        val result = makerWorldModel.toResultEntity(parentId = 20)

        assertEquals(20, result.parentId)
        assertEquals(ModelType.MAKER_WORLD.value, result.sectionName)
        assertEquals("Maker Model", result.title)
        assertEquals(50, result.likeCount)
        assertEquals("https://maker.url", result.imageUrl)
        assertEquals("https://makerworld.com/en/models/123", result.contentUrl)
    }

    @Test
    fun `ResultEntity asModelItem maps correctly`() {
        val entity = ResultEntity(
            id = 1,
            parentId = 10,
            sectionName = "sketchFab",
            title = "Entity Title",
            likeCount = 75,
            imageUrl = "https://entity.url",
            contentUrl = "https://content.url"
        )

        val modelItem = entity.asModelItem(isFavorite = true)

        assertEquals(1, modelItem.id)
        assertEquals("sketchFab", modelItem.sectionName)
        assertEquals("Entity Title", modelItem.title)
        assertEquals(75, modelItem.likeCount)
        assertEquals("https://content.url", modelItem.url)
        assertEquals(true, modelItem.isFavorite)
        assertEquals("https://entity.url", modelItem.thumbnails.first().url)
    }
}
