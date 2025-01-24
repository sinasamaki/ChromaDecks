package com.sinasamaki.chromadecks.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

fun Path.lineTo(to: Offset) = lineTo(to.x, to.y)
fun Path.moveTo(to: Offset) = moveTo(to.x, to.y)
fun Path.relativeLineTo(to: Offset) = relativeLineTo(to.x, to.y)