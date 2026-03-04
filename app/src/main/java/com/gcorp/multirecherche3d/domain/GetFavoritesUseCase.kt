package com.gcorp.multirecherche3d.domain

import com.gcorp.multirecherche3d.data.GetModelItemsRepository
import com.gcorp.multirecherche3d.domain.model.ModelItem
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val getModelItemsRepository: GetModelItemsRepository
) {

    suspend fun getFavorites(ids: List<Int>): List<ModelItem> = getModelItemsRepository.getItems(ids)
}