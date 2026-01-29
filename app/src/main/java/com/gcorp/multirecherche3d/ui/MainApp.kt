package com.gcorp.multirecherche3d.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.gcorp.multirecherche3d.domain.model.ModelItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val viewModel: MainAppViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface {
        Scaffold(
            modifier = Modifier.padding(20.dp),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Greg") }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                item {
                    var searchText by remember { mutableStateOf("poulpe") }
                    SearchBar(
                        inputField = {
                            InputField(
                                modifier = Modifier.fillMaxWidth(),
                                query = searchText,
                                onQueryChange = { searchText = it },
                                onSearch = { viewModel.multiSearch(searchText) },
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

                item {
                    if (uiState.searchResults.isNotEmpty()) {
                        Text(
                            text = "SketchFab",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }

                    LazyRow(
                        modifier = Modifier.height(300.dp)
                    ) {
                        items(uiState.searchResults) {
                            ModelItemCard(item = it)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModelItemCard(
    modifier: Modifier = Modifier,
    item: ModelItem
) {
    val painter = rememberAsyncImagePainter( item.thumbnails.firstOrNull()?.url?.toString())
    val state by painter.state.collectAsStateWithLifecycle()

    Card(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .width(200.dp)
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        when(state) {
            is AsyncImagePainter.State.Empty,
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer()
                )
            }
            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = Modifier.weight(1f),
                    painter = painter,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }
            is AsyncImagePainter.State.Error -> {
                // Show some error UI.
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Likes : ${item.likeCount}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}