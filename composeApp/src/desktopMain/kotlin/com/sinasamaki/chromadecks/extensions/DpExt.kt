package com.sinasamaki.chromadecks.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


inline val Int.dpPx: Float
    @Composable get()  {
        return with(LocalDensity.current) { this@dpPx.dp.toPx() }
    }