package com.sinasamaki.chromadecks.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class CodeColors(
    val keyword: Color = Violet300,
    val string: Color = Green300,
    val number: Color = Amber300,
    val function: Color = Sky300,
    val param: Color = Teal300,
    val comment: Color = Slate500,
)

val LocalCodeColors = compositionLocalOf<CodeColors> { error("No code colors provided") }
