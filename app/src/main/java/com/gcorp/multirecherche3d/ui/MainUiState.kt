package com.gcorp.multirecherche3d.ui

import com.gcorp.multirecherche3d.domain.model.ModelItem

data class MainUiState(
    val isLoading: Boolean = false,
    val sketchFabResults: List<ModelItem> = emptyList(),
    val makerWorldResults: List<ModelItem> = emptyList()
)
