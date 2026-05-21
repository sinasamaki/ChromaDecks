package com.sinasamaki.kotlinconf.logo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

const val strokeWidth = .5f
val brush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFF2EF),
        Color(0xFFDAB6EA),
    ),
    start = Offset(0f, 128f),
    end = Offset(128f, 0f),
)
