package com.sinasamaki.chromadecks

import App
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    state = WindowState(
        placement = WindowPlacement.Maximized,
    ),
    title = "ChromaDecks"
) {
    App()
}
