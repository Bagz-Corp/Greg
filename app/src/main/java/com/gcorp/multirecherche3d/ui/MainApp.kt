package com.gcorp.multirecherche3d.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.gcorp.multirecherche3d.ui.designSystem.GregTopAppBar
import com.gcorp.multirecherche3d.ui.designSystem.ResultCard
import com.gcorp.multirecherche3d.ui.theme.GregTheme
import com.gcorp.multirecherche3d.ui.theme.OffWhite
import com.gcorp.multirecherche3d.ui.theme.SoftGrey
import com.gcorp.multirecherche3d.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp () {
    val viewModel: MainAppViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface {
        Scaffold(
            modifier = Modifier,
            topBar = { GregTopAppBar() }
        ) { paddingValues ->
            MainResultScreen(
                modifier = Modifier.padding(paddingValues),
                onSearch = { query ->
                    keyboardController?.hide()
                    viewModel.updateQuery(query)
                },
                onResultTap = { uri ->
                    viewModel.customTabsIntent.launchUrl(context, uri)
                },
                onFavorite = { id, state ->
                    viewModel.updateFavorite(id, state)
                },
                sketchFabResults = uiState.sketchFabResults,
                makerWorldResults = uiState.makerWorldResults
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GregSearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("poulpe") }
    SearchBar(
        inputField = {
            InputField(
                modifier = modifier.fillMaxWidth(),
                query = searchText,
                onQueryChange = { searchText = it },
                onSearch = onSearch,
                expanded = false,
                onExpandedChange = {},
                placeholder = { Text("Rechercher...") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Rechercher",
                    )
                },
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = Modifier,
        colors = SearchBarDefaults.colors(
            containerColor = SoftGrey
        )
    ) {}
}

@Composable
fun MainResultScreen(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onResultTap: (Uri) -> Unit,
    onFavorite: (Int, Boolean) -> Unit,
    sketchFabResults: List<ModelItem>,
    makerWorldResults: List<ModelItem>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .gradientSurface(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        item { GregSearchBar(onSearch = onSearch) }

        item {
            Column (modifier = Modifier.padding(vertical = 8.dp)) {
                if (sketchFabResults.isNotEmpty()) {
                    Section(
                        results = sketchFabResults,
                        modelType = ModelType.SKETCH_FAB,
                        onResultTap = onResultTap,
                        onFavorite = onFavorite
                    )
                }

                if (makerWorldResults.isNotEmpty()) {
                    Section(
                        results = makerWorldResults,
                        modelType = ModelType.MAKER_WORLD,
                        onResultTap = onResultTap,
                        onFavorite = onFavorite
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title.uppercase(),
        style = Typography.headlineSmall,
        modifier = modifier,
        color = Color.Black
    )
}

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    results: List<ModelItem> = emptyList(),
    onCardClick: (String) -> Unit,
    onFavorite: (Int, Boolean) -> Unit
) {
    val rowState = rememberLazyListState()
    LaunchedEffect(results) {
        rowState.animateScrollToItem(0)
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Responsive to screens, we aim to display 3 cards at all times
        val size = maxOf((maxWidth.div(3)), 100.dp)

        LazyRow(
            modifier = modifier.padding(vertical = 16.dp),
            state = rowState
        ) {
            items(results) {
                ResultCard(
                    cardData = it,
                    onClick = onCardClick,
                    onFavorite = onFavorite
                )
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
            }
        }
    }


}

@Composable
fun Section(
    modifier: Modifier = Modifier,
    results: List<ModelItem>,
    modelType: ModelType,
    onResultTap: (Uri) -> Unit,
    onFavorite: (Int, Boolean) -> Unit
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        SearchResultTitle(
            title = modelType.value
        )

        SearchResults(
            results = results,
            onCardClick = { url ->
                onResultTap(url.toUri())
            },
            onFavorite = onFavorite
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = OffWhite
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainPreview() {
    GregTheme {
        Scaffold(
            topBar = { GregTopAppBar() }
        ) { paddingValues ->
            MainResultScreen(
                modifier = Modifier.padding(paddingValues),
                onSearch = {},
                onResultTap = {},
                sketchFabResults = modelItems,
                makerWorldResults = modelItems,
                onFavorite = {_, _ ->}
            )
        }
    }
}

// preview items
private val modelItems by lazy {
    List(4) {
        ModelItem(
            id = 42,
            thumbnails = listOf(
                Thumbnail(
                    url = "someUrl",
                    width = 42,
                    height = 42
                )
            ),
            title = "Card title",
            likeCount = 42,
            url = "some string",
            sectionName = "Section Name",
            isFavorite = false
        )
    }
}