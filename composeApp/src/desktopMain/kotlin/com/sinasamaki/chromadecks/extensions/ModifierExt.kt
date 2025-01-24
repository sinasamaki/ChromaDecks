package com.sinasamaki.chromadecks.extensions

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.center

@Composable
fun Modifier.centerHorizontally(): Modifier {

    var centerOffset by remember {
        mutableStateOf(IntOffset.Zero)
    }
    return this
        .onSizeChanged {
            centerOffset = it.center
        }
        .offset {
            IntOffset(-centerOffset.x, 0)
        }
}