package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gcorp.multirecherche3d.ui.theme.Typography

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Search, "Recherche")
    object Favorites : Screen("favorites", Icons.Default.Favorite, "Favoris")
}

@Composable
fun MainBottomBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val screens = listOf(Screen.Home, Screen.Favorites)
    
    NavigationBar(
        tonalElevation = 8.dp
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(screen.label, style = Typography.labelMedium) },
            )
        }
    }
}
