package com.gcorp.multirecherche3d.data

import app.cash.turbine.test
import com.gcorp.multirecherche3d.database.dao.QueryDao
import com.gcorp.multirecherche3d.database.entity.QueryEntity
import com.gcorp.multirecherche3d.database.entity.QueryWithResults
import com.gcorp.multirecherche3d.database.entity.ResultEntity
import com.gcorp.multirecherche3d.domain.GetFavoritesUseCase
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.network.RemoteDataSource
import com.gcorp.multirecherche3d.network.model.MakerWorldModel
import com.gcorp.multirecherche3d.network.model.SketchFabModel
import com.gcorp.shared.PreferencesDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryTest {

    private val network = mockk<RemoteDataSource>()
    private val searchQueryDao = mockk<QueryDao>()
    private val preferenceDataSource = mockk<PreferencesDataSource>()
    private val getFavoritesUseCase = mockk<GetFavoritesUseCase>()

    private lateinit var repository: SearchRepository

    private val queryResultsFlow = MutableStateFlow<QueryWithResults?>(null)
    private val favoriteIdsFlow = MutableStateFlow<List<Int>>(emptyList())

    @Before
    fun setup() {
        every { searchQueryDao.getQueryResults(any()) } returns queryResultsFlow
        every { preferenceDataSource.favoriteIds } returns favoriteIdsFlow
        coEvery { getFavoritesUseCase.getFavorites(any()) } returns emptyList()

        repository = SearchRepository(
            network,
            searchQueryDao,
            preferenceDataSource,
            getFavoritesUseCase
        )
    }

    @Test
    fun `results flow combines data correctly`() = runTest {
        val mockQueryResults = QueryWithResults(
            query = QueryEntity(uid = 1, searchQuery = "test"),
            results = listOf(
                ResultEntity(1, 1, "sketchFab", "Title 1", 10, "url1", "content1"),
                ResultEntity(2, 1, "makerWorld", "Title 2", 20, "url2", "content2")
            )
        )
        val mockFavorites = listOf(
            mockk<ModelItem> {
                every { id } returns 3
                every { title } returns "Fav 1"
                every { isFavorite } returns true
            }
        )

        // Préparer les réponses MockK AVANT de déclencher les émissions
        coEvery { getFavoritesUseCase.getFavorites(listOf(1, 3)) } returns mockFavorites

        repository.results.test {
            assertEquals(0, awaitItem()?.size)

            queryResultsFlow.value = mockQueryResults
            var currentResults = awaitItem()
            
            favoriteIdsFlow.value = listOf(1, 3)
            currentResults = awaitItem()

            assertEquals(3, currentResults?.size)

            val item1 = currentResults?.find { it.id == 1 }
            assertEquals(true, item1?.isFavorite)

            val item2 = currentResults?.find { it.id == 2 }
            assertEquals(false, item2?.isFavorite)

            val item3 = currentResults?.find { it.id == 3 }
            assertEquals("Fav 1", item3?.title)
        }
    }

    @Test
    fun `updateQuery fetches and saves results`() = runTest {
        val query = "robot"
        val mockSketchFab = listOf<SketchFabModel>(mockk(relaxed = true))
        val mockMakerWorld = listOf<MakerWorldModel>(mockk(relaxed = true))
        val mockQueryEntity = QueryEntity(uid = 10, searchQuery = query)

        coEvery { network.fetchSketchFab(query) } returns mockSketchFab
        coEvery { network.fetchMakerWorld(query) } returns mockMakerWorld
        coEvery { searchQueryDao.insertQuery(any()) } returns Unit
        every { searchQueryDao.getQuery(query) } returns mockQueryEntity
        coEvery { searchQueryDao.insertResults(any()) } returns Unit

        repository.updateQuery(query)

        coVerify { network.fetchSketchFab(query) }
        coVerify { network.fetchMakerWorld(query) }
        coVerify { searchQueryDao.insertQuery(any()) }
        coVerify { searchQueryDao.insertResults(any()) }
    }

    @Test
    fun `updateQuery handles network errors gracefully`() = runTest {
        val query = "error"
        coEvery { network.fetchSketchFab(query) } throws Exception("Network Error")

        repository.updateQuery(query)
        // Vérifie simplement que le crash est évité
    }
}
