package com.gcorp.multirecherche3d.ui

import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcorp.multirecherche3d.data.ModelType
import com.gcorp.multirecherche3d.domain.GetSearchResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val searchUseCase: GetSearchResultsUseCase,
): ViewModel() {

    @Inject lateinit var customTabsIntent: CustomTabsIntent

    val uiState: StateFlow<MainUiState> = searchUseCase
        .getResults()
        .map {
            MainUiState(
                isLoading = false,
                sketchFabResults = it?.filter { it.sectionName == ModelType.SKETCH_FAB.value } ?: emptyList(),
                makerWorldResults = it?.filter { it.sectionName == ModelType.MAKER_WORLD.value } ?: emptyList()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState(isLoading = true)
        )

    fun updateQuery(searchQuery: String) = viewModelScope.launch {
        searchUseCase.updateQuery(searchQuery)
    }

}