package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gcorp.multirecherche3d.ui.theme.SageGreen
import com.gcorp.multirecherche3d.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GregTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(
            text = "Greg",
            style = Typography.headlineLarge
        ) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = SageGreen
        )
    )
}