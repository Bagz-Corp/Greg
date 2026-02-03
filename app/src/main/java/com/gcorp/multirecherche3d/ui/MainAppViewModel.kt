package com.gcorp.multirecherche3d.ui

import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcorp.multirecherche3d.domain.MultiSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val searchUseCase: MultiSearchUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    @Inject lateinit var customTabsIntent: CustomTabsIntent

    fun multiSearch(searchQuery: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        val results = searchUseCase(searchQuery)

        _uiState.update {
            it.copy(
                isLoading = false,
                searchResults = results
            )
        }
    }

}