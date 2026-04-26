package com.sinasamaki.chromadecks.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@ReadOnlyComposable
@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }

@ReadOnlyComposable
@Composable
fun Dp.roundToPx(): Int = with(LocalDensity.current) { this@roundToPx.roundToPx() }


@ReadOnlyComposable
@Composable
fun Int.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

@ReadOnlyComposable
@Composable
fun Float.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

@ReadOnlyComposable
@Composable
fun Double.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }