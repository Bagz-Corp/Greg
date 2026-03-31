package com.gcorp.multirecherche3d.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.ModelType
import com.gcorp.multirecherche3d.domain.model.Thumbnail
import com.gcorp.multirecherche3d.ui.designSystem.GregSearchBar
import com.gcorp.multirecherche3d.ui.designSystem.MainBottomBar
import com.gcorp.multirecherche3d.ui.designSystem.Screen
import com.gcorp.multirecherche3d.ui.designSystem.Section
import com.gcorp.multirecherche3d.ui.favorites.FavoritesScreen
import com.gcorp.multirecherche3d.ui.theme.GregTheme

@Composable
fun MainApp() {
    val viewModel: MainAppViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Surface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            // topBar = { GregTopAppBar() },
            bottomBar = {
                MainBottomBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (currentScreen) {
                    Screen.Home -> MainResultScreen(
                        onSearch = { query ->
                            keyboardController?.hide()
                            viewModel.updateQuery(query)
                        },
                        onResultTap = { url ->
                            viewModel.customTabsIntent.launchUrl(context, url.toUri())
                        },
                        onFavorite = { id, state ->
                            viewModel.updateFavorite(id, state)
                        },
                        sketchFabResults = uiState.sketchFabResults,
                        makerWorldResults = uiState.makerWorldResults
                    )
                    Screen.Favorites -> FavoritesScreen(
                        favorites = uiState.favorites,
                        isLoading = uiState.isLoading,
                        onResultTap = { uri ->
                            viewModel.customTabsIntent.launchUrl(context, uri)
                        },
                        onFavorite = { id, state ->
                            viewModel.updateFavorite(id, state)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainResultScreen(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onResultTap: (String) -> Unit,
    onFavorite: (Int, Boolean) -> Unit,
    sketchFabResults: List<ModelItem>,
    makerWorldResults: List<ModelItem>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .gradientSurface(),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        item { GregSearchBar(onSearch = onSearch) }

        item {
            Column (modifier = Modifier.padding(vertical = 8.dp)) {
                if (sketchFabResults.isNotEmpty()) {
                    Section(
                        results = sketchFabResults,
                        modelType = ModelType.SKETCH_FAB,
                        onCardClick = onResultTap,
                        onFavorite = onFavorite
                    )
                }

                if (makerWorldResults.isNotEmpty()) {
                    Section(
                        results = makerWorldResults,
                        modelType = ModelType.MAKER_WORLD,
                        onCardClick = onResultTap,
                        onFavorite = onFavorite
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainResultScreenPreview() {
    GregTheme {
        MainResultScreen(
            onSearch = {},
            onResultTap = {},
            onFavorite = { _, _ -> },
            sketchFabResults = List(3) {
                ModelItem(
                    id = it,
                    sectionName = "SketchFab",
                    thumbnails = listOf(Thumbnail("url", 100, 100)),
                    title = "Sketch Model $it",
                    likeCount = 50,
                    url = "url",
                    isFavorite = false
                )
            },
            makerWorldResults = List(3) {
                ModelItem(
                    id = it + 10,
                    sectionName = "MakerWorld",
                    thumbnails = listOf(Thumbnail("url", 100, 100)),
                    title = "Maker Model $it",
                    likeCount = 80,
                    url = "url",
                    isFavorite = false
                )
            }
        )
    }
}
