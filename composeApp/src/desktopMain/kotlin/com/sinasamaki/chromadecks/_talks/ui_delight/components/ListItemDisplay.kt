package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.components.CodeIDE

@Composable
fun ListItemDisplay(
    modifier: Modifier = Modifier,
    code: String,
    tabName: String = "ListItem.kt",
    implementation: @Composable () -> Unit,
) {

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CodeIDE(
            tabs = listOf(tabName to code),
            selectedTab = 0,
            onTabSelect = {},
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(1f).padding(24.dp),
        )
        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .padding(32.dp)
                .fillMaxHeight()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val density = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(density.density * 1.5f, density.fontScale * 1.5f)
            ) {
                implementation()
            }
        }
    }

}