package com.gcorp.multirecherche3d.ui

data class MainUiState(
    val isLoading: Boolean = false,
    val searchResults: List<String> = emptyList()
)
