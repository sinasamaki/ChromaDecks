package com.sinasamaki.chromadecks

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() = application {
    Window(
        state = rememberWindowState(
            placement = WindowPlacement.Maximized
        ),
        onCloseRequest = ::exitApplication,
        title = "ChromaDecks",
    ) {
        DevelopmentEntryPoint {
            App()
        }
    }
}