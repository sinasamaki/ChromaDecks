package com.sinasamaki.chromadecks.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun RowScope.Space(
    space: Dp
) = Spacer(Modifier.width(space))

@Composable
fun ColumnScope.Space(
    space: Dp
) = Spacer(Modifier.height(space))

@Composable
fun RowScope.Space(
    weight: Float
) = Spacer(Modifier.weight(weight))

@Composable
fun ColumnScope.Space(
    weight: Float
) = Spacer(Modifier.weight(weight))
