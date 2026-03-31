package com.gcorp.multirecherche3d.ui

import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcorp.multirecherche3d.data.ModelType
import com.gcorp.multirecherche3d.domain.GetFavoritesUseCase
import com.gcorp.multirecherche3d.domain.GetSearchResultsUseCase
import com.gcorp.shared.PreferencesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val searchUseCase: GetSearchResultsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val preferencesDataSource: PreferencesDataSource
): ViewModel() {

    @Inject lateinit var customTabsIntent: CustomTabsIntent

    /** Favorites are set but there is no section yet for them, to be added later */
    val uiState: StateFlow<MainUiState> = searchUseCase.getResults().combine(
        preferencesDataSource.favoriteIds
    ) { searchResults, favoriteIds ->
        val favorites = getFavoritesUseCase.getFavorites(favoriteIds)

        MainUiState(
            isLoading = false,
            favorites = favorites,
            sketchFabResults = searchResults?.filter { it.sectionName == ModelType.SKETCH_FAB.value } ?: emptyList(),
            makerWorldResults = searchResults?.filter { it.sectionName == ModelType.MAKER_WORLD.value } ?: emptyList()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState(isLoading = true)
    )

    fun updateQuery(searchQuery: String) = viewModelScope.launch {
        searchUseCase.updateQuery(searchQuery)
    }

    fun updateFavorite(id: Int, state: Boolean) = viewModelScope.launch {
        println("Item with id $id set as favorite $state")
        if (state) {
            preferencesDataSource.addFavorite(id)
        } else {
            preferencesDataSource.removeFavorite(id)
        }
    }
}