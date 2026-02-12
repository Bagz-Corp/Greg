package com.gcorp.multirecherche3d.ui

import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

//    private val _uiState = MutableStateFlow(MainUiState())
  //  val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    @Inject lateinit var customTabsIntent: CustomTabsIntent

    val uiState: StateFlow<MainUiState> = searchUseCase.getResults()
        .map {
            MainUiState(
                isLoading = false,
                searchResults = it ?: emptyList()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState(isLoading = true)
        )

    fun multiSearch(searchQuery: String) = viewModelScope.launch {
        searchUseCase.updateQuery(searchQuery)
    }

}