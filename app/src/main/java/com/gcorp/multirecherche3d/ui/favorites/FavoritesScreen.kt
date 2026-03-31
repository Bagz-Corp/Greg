package com.gcorp.multirecherche3d.ui.favorites

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.Thumbnail
import com.gcorp.multirecherche3d.ui.designSystem.ResultCard
import com.gcorp.multirecherche3d.ui.gradientSurface
import com.gcorp.multirecherche3d.ui.theme.Charcoal
import com.gcorp.multirecherche3d.ui.theme.GregTheme
import com.gcorp.multirecherche3d.ui.theme.Typography

@Composable
fun FavoritesScreen(
    favorites: List<ModelItem>,
    isLoading: Boolean,
    onResultTap: (Uri) -> Unit,
    onFavorite: (Int, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .gradientSurface()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Mes Favoris",
            style = Typography.headlineMedium,
            color = Charcoal
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading && favorites.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (favorites.isEmpty()) {
                Text(
                    text = "Aucun favori pour le moment",
                    style = Typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(favorites, key = { it.id }) { item ->
                        ResultCard(
                            modifier = Modifier.fillMaxWidth(),
                            cardData = item,
                            onClick = { url -> onResultTap(url.toUri()) },
                            onFavorite = onFavorite
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    GregTheme {
        FavoritesScreen(
            favorites = List(4) {
                ModelItem(
                    id = it,
                    sectionName = "MakerWorld",
                    thumbnails = listOf(Thumbnail("url", 100, 100)),
                    title = "Favorite Model $it",
                    likeCount = 100,
                    url = "url",
                    isFavorite = true
                )
            },
            isLoading = false,
            onResultTap = {},
            onFavorite = { _, _ -> }
        )
    }
}
