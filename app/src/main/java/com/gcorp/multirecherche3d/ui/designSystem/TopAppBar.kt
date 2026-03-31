package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gcorp.multirecherche3d.ui.theme.GregTheme
import com.gcorp.multirecherche3d.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GregTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = "Greg",
                style = Typography.headlineLarge
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    GregTheme(
        darkTheme = false
    ) {
        GregTopAppBar()
    }
}