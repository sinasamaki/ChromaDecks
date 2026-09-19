package com.sinasamaki.chromadecks._001_MeshGradients

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sinasamaki.chromadecks._001_MeshGradients.slides.CodeSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.DefinitionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.EndSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.FunctionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.HighResolutionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.LowResolutionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.PathInterpolationSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.TitleSlide
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.Zinc900


fun main() = application {
    Window(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "ChromaDecks",
        onCloseRequest = ::exitApplication,
        content = {
            ChromaContainer {
                MeshGradientPresentation()
            }
        },
    )
}


@Composable
fun MeshGradientPresentation(modifier: Modifier = Modifier) {

    SlidesPresenter(
        modifier = modifier
            .background(
                color = Zinc900
            ),
        slides = remember {
            listOf(
                LowResolutionSlide(),

                TitleSlide(),
                DefinitionSlide(),
                FunctionSlide(),
                CodeSlide(),
                LowResolutionSlide(),
                PathInterpolationSlide(),
                HighResolutionSlide(),
                EndSlide(),
            )
        }
    )

}