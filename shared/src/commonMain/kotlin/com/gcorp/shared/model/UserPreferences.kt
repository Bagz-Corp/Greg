package com.gcorp.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val favoriteIds: List<Int> = emptyList()
)
