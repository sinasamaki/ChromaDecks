package com.sinasamaki.chromadecks.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


inline val Int.dpPx: Float
    @Composable get()  {
        return with(LocalDensity.current) { this@dpPx.dp.toPx() }
    }

inline val Int.pxDp: Dp
    @Composable get()  {
        return with(LocalDensity.current) { this@pxDp.toDp() }
    }

inline val Dp.toPx: Float
    @Composable get()  {
        return with(LocalDensity.current) { this@toPx.toPx() }
    }