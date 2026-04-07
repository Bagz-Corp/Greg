package com.gcorp.multirecherche3d.ui

import app.cash.turbine.test
import com.gcorp.multirecherche3d.data.ModelType
import com.gcorp.multirecherche3d.domain.GetFavoritesUseCase
import com.gcorp.multirecherche3d.domain.GetSearchResultsUseCase
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.shared.PreferencesDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainAppViewModelTest {

    private val searchUseCase = mockk<GetSearchResultsUseCase>()
    private val getFavoritesUseCase = mockk<GetFavoritesUseCase>()
    private val preferencesDataSource = mockk<PreferencesDataSource>()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MainAppViewModel

    private val searchFlow = MutableStateFlow<List<ModelItem>?>(null)
    private val favoritesIdsFlow = MutableStateFlow<List<Int>>(emptyList())

    val mockItems = listOf(
        ModelItem(1, ModelType.SKETCH_FAB.value, emptyList(), "Sketch", 10, "url1", false),
        ModelItem(2, ModelType.MAKER_WORLD.value, emptyList(), "Maker", 20, "url2", false)
    )
    val mockFavorites = listOf(
        ModelItem(3, ModelType.SKETCH_FAB.value, emptyList(), "Fav", 5, "url3", true)
    )


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { searchUseCase.getResults() } returns searchFlow
        every { preferencesDataSource.favoriteIds } returns favoritesIdsFlow
        coEvery { getFavoritesUseCase.getFavorites(any()) } returns emptyList()

        viewModel = MainAppViewModel(
            searchUseCase,
            getFavoritesUseCase,
            preferencesDataSource
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `uiState updates when search results and favorites change`() = runTest {
        viewModel.uiState.test {
            // Initial item (loading)
            val initialItem = awaitItem()
            assertEquals(true, initialItem.isLoading)

            // Update search flow
            searchFlow.value = mockItems

            var updatedItem = awaitItem()
            assertEquals(false, updatedItem.isLoading)
            assertEquals(1, updatedItem.sketchFabResults.size)
            assertEquals(1, updatedItem.makerWorldResults.size)
            assertEquals(0, updatedItem.favorites.size)

            // Update favorites
            favoritesIdsFlow.value = listOf(3)
            coEvery { getFavoritesUseCase.getFavorites(listOf(3)) } returns mockFavorites

            updatedItem = awaitItem()
            assertEquals(false, updatedItem.isLoading)
            assertEquals(1, updatedItem.sketchFabResults.size)
            assertEquals(1, updatedItem.makerWorldResults.size)
            assertEquals(1, updatedItem.favorites.size)
            assertEquals(mockFavorites.first().id, updatedItem.favorites.first().id)
        }
    }

    @Test
    fun `updateQuery calls searchUseCase`() = runTest {
        coEvery { searchUseCase.updateQuery("query") } returns Unit
        
        viewModel.updateQuery("query")
        advanceUntilIdle()
        
        coVerify { searchUseCase.updateQuery("query") }
    }

    @Test
    fun `updateFavorite adding favorite calls preferencesDataSource`() = runTest {
        coEvery { preferencesDataSource.addFavorite(1) } returns Unit
        
        viewModel.updateFavorite(1, true)
        advanceUntilIdle()
        
        coVerify { preferencesDataSource.addFavorite(1) }
    }

    @Test
    fun `updateFavorite removing favorite calls preferencesDataSource`() = runTest {
        coEvery { preferencesDataSource.removeFavorite(1) } returns Unit
        
        viewModel.updateFavorite(1, false)
        advanceUntilIdle()
        
        coVerify { preferencesDataSource.removeFavorite(1) }
    }
}
