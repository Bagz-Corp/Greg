package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.ModelType
import com.gcorp.multirecherche3d.ui.theme.Charcoal
import com.gcorp.multirecherche3d.ui.theme.OffWhite
import com.gcorp.multirecherche3d.ui.theme.SoftGrey
import com.gcorp.multirecherche3d.ui.theme.Typography

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
        colors = SearchBarDefaults.colors(
            containerColor = OffWhite
        )
    ) {}
}

@Composable
fun SearchResultTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = modifier,
        color = Charcoal
    )
}

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    results: List<ModelItem> = emptyList(),
    onCardClick: (String) -> Unit,
    onFavorite: (Int, Boolean) -> Unit
) {
    LazyRow(
        modifier = modifier.padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(results) {
            ResultCard(
                cardData = it,
                onClick = onCardClick,
                onFavorite = onFavorite
            )
        }
    }
}

@Composable
fun Section(
    modifier: Modifier = Modifier,
    results: List<ModelItem>,
    modelType: ModelType,
    onCardClick: (String) -> Unit,
    onFavorite: (Int, Boolean) -> Unit
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        SearchResultTitle(
            title = modelType.value
        )

        SearchResults(
            results = results,
            onCardClick = onCardClick,
            onFavorite = onFavorite
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = SoftGrey.copy(alpha = 0.5f)
        )
    }
}
