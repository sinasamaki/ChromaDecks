package com.sinasamaki.chromadecks.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

operator fun Offset.times(size: Size): Offset = Offset(x * size.width, y * size.height)


operator fun Offset.div(size: Size): Offset = Offset(x / size.width, y / size.height)


operator fun MutableList<Offset>.times(size: Size): List<Offset> {
    return map { Offset(it.x * size.width, it.y * size.height) }
}