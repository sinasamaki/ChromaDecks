package com.sinasamaki.chromadecks._004_TimelyTimer

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sinasamaki.chromadecks._004_TimelyTimer.components.RadialDriftBackground
import com.sinasamaki.chromadecks._004_TimelyTimer.slides.BackgroundExplorationSlide
import com.sinasamaki.chromadecks._004_TimelyTimer.slides.SimpleDialSlide
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.slideanimations.translateOutX
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.CodeColors
import com.sinasamaki.chromadecks.ui.theme.Green
import com.sinasamaki.chromadecks.ui.theme.Orange
import com.sinasamaki.chromadecks.ui.theme.Purple
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import com.sinasamaki.chromadecks.ui.theme.plus


fun main() = application {
    Window(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "ChromaDecks",
        onCloseRequest = ::exitApplication,
        content = {
            TimelyTimerPresentation()
        },
    )
}

fun timelySwatch(index: Int): Swatch = Green + index


@Composable
fun TimelyTimerPresentation() {
    var currentIndex by remember { mutableStateOf(0) }
    val swatch = timelySwatch(currentIndex)

    val keyword by animateColorAsState(swatch.v300, tween(800), label = "code-keyword")
    val function by animateColorAsState((swatch + 1).v300, tween(800), label = "code-function")
    val string by animateColorAsState((swatch + 2).v300, tween(800), label = "code-string")
    val number by animateColorAsState(swatch.v200, tween(800), label = "code-number")
    val param by animateColorAsState((swatch + 1).v200, tween(800), label = "code-param")
    val comment by animateColorAsState(swatch.v500, tween(800), label = "code-comment")

    ChromaContainer(
        codeColors = CodeColors(
            keyword = keyword,
            string = string,
            number = number,
            function = function,
            param = param,
            comment = comment,
        ),
    ) {
        Scaffold(
            containerColor = Zinc900,
            contentColor = Slate50,
        ) {
            SlidesPresenter2(
                modifier = Modifier,
                slides = remember {
                    listOf(
                        SimpleDialSlide(),
                        BackgroundExplorationSlide(),
                    )
                },
                onCurrentIndexChange = { currentIndex = it },
                background = {
                    RadialDriftBackground(
                        modifier = Modifier.fillMaxSize(),
                        centerColor = swatch.v600,
                        midColor = (swatch + 1).v400,
                        edgeColor = (swatch + 2).v200,
                        index = currentIndex,
                    )
                },
                animator = { content ->
                    Box(
                        Modifier
                            .parallax(factor = 1f)
                            .translateInX(initial = 1f)
                            .translateOutX(initial = -.7f)
                            .fadeIn()
                            .fadeOut()
                    ) {
                        content()
                    }
                }
            )
        }
    }
}
