package com.sinasamaki.chromadecks.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun RowScope.Space(
    space: Dp
) {
    Box(
        Modifier
            .width(space)
    )
}

@Composable
fun ColumnScope.Space(
    space: Dp
) {
    Box(
        Modifier
            .height(space)
    )
}