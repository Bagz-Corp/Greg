package com.gcorp.multirecherche3d.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.ui.designSystem.GregTopAppBar
import com.gcorp.multirecherche3d.ui.designSystem.ResultCard
import com.gcorp.multirecherche3d.ui.theme.GregTheme
import com.gcorp.multirecherche3d.ui.theme.OffWhite
import com.gcorp.multirecherche3d.ui.theme.SageGreen
import com.gcorp.multirecherche3d.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp () {
    val viewModel: MainAppViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Surface {
        Scaffold(
            modifier = Modifier.background(color = SageGreen),
            topBar = { GregTopAppBar() }
        ) { paddingValues ->
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = SageGreen),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                item { GregSearchBar(onSearch = viewModel::multiSearch) }

                item {
                    if (uiState.searchResults.isNotEmpty()) {
                        SearchResultTitle()
                    }

                    SearchResults(
                        results = uiState.searchResults,
                        onCardClick = { url ->
                            viewModel.customTabsIntent.launchUrl(context, url.toUri())
                        }
                    )

                    HorizontalDivider(
                        thickness = Dp.Hairline,
                        color = OffWhite
                    )
                }
            }
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
                onQueryChange = { },
                onSearch = onSearch,
                expanded = false,
                onExpandedChange = {},
                placeholder = { Text("Rechercher...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Rechercher",
                    )
                },
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = Modifier,
    ) {}
}

@Composable
fun SearchResultTitle(modifier: Modifier = Modifier) {
    Text(
        text = "SketchFab",
        style = Typography.displayMedium,
        modifier = modifier.padding(16.dp)
    )
}

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    results: List<ModelItem> = emptyList(),
    onCardClick: (String) -> Unit
) {
    LazyRow(
        modifier = modifier.height(300.dp)
    ) {
        items(results) {
            ResultCard(
                cardData = it,
                onClick = onCardClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MainPreview() {
    GregTheme {
        Scaffold(
            modifier = Modifier.background(color = SageGreen),
            topBar = { GregTopAppBar() }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = SageGreen),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                item { GregSearchBar(onSearch = {}) }

                item {
                    SearchResultTitle()
                    val items = List(4) {
                        ModelItem(
                            thumbnails = emptyList(),
                            title = "Card title",
                            likeCount = 42,
                            url = "some string"
                        )
                    }
                    SearchResults(
                        results = items,
                        onCardClick = {}
                    )

                    HorizontalDivider(
                        thickness = Dp.Hairline,
                        color = OffWhite
                    )
                }
            }
        }
    }
}