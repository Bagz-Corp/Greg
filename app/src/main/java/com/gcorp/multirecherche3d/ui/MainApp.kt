package com.gcorp.multirecherche3d.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.Thumbnail
import com.gcorp.multirecherche3d.ui.designSystem.GregTopAppBar
import com.gcorp.multirecherche3d.ui.designSystem.ResultCard
import com.gcorp.multirecherche3d.ui.theme.Charcoal
import com.gcorp.multirecherche3d.ui.theme.GregTheme
import com.gcorp.multirecherche3d.ui.theme.OffWhite
import com.gcorp.multirecherche3d.ui.theme.SageGreen
import com.gcorp.multirecherche3d.ui.theme.Typography
import java.net.URI

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
            MainResultScreen(
                modifier = Modifier.padding(paddingValues),
                onSearch = viewModel::multiSearch,
                onViewModel = { uri ->
                    viewModel.customTabsIntent.launchUrl(context, uri)
                },
                searchResults = uiState.searchResults,
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
    ) {}
}

@Composable
fun MainResultScreen(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onViewModel: (Uri) -> Unit,
    searchResults: List<ModelItem>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = SageGreen),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        item { GregSearchBar(onSearch = onSearch) }

        item {
            Column (modifier = Modifier.padding(vertical = 8.dp)) {
                if (searchResults.isNotEmpty()) {
                    SearchResultTitle()
                }

                SearchResults(
                    results = searchResults,
                    onCardClick = { url ->
                        onViewModel(url.toUri())
                    }
                )

                if (searchResults.isNotEmpty()) {
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = OffWhite
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultTitle(modifier: Modifier = Modifier) {
    Text(
        text = "SketchFab",
        style = Typography.headlineMedium,
        modifier = modifier,
        color = Charcoal
    )
}

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    results: List<ModelItem> = emptyList(),
    onCardClick: (String) -> Unit
) {
    val rowState = rememberLazyListState()
    LaunchedEffect(results) {
        rowState.animateScrollToItem(0)
    }

    LazyRow(
        modifier = modifier.padding(vertical = 16.dp),
        state = rowState
    ) {
        items(results) {
            ResultCard(
                cardData = it,
                onClick = onCardClick
            )
            Spacer(
                modifier = Modifier.width(4.dp)
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
            MainResultScreen(
                modifier = Modifier.padding(paddingValues),
                onSearch = {},
                onViewModel = {},
                searchResults = modelItems
            )
        }
    }
}

private val modelItems by lazy {
    List(4) {
        ModelItem(
            thumbnails = listOf(
                Thumbnail(
                    url = URI("someUrl"),
                    width = 42,
                    height = 42
                )
            ),
            title = "Card title",
            likeCount = 42,
            url = "some string"
        )
    }
}